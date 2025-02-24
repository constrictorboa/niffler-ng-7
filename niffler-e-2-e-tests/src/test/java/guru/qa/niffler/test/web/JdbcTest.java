package guru.qa.niffler.test.web;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.impl.AuthAuthorityDaoJdbc;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.entity.userdata.UserdataUserEntity;
import guru.qa.niffler.data.repository.impl.AuthUserRepositoryHibernate;
import guru.qa.niffler.data.repository.impl.AuthUserRepositoryJdbc;
import guru.qa.niffler.data.repository.impl.AuthUserRepositorySpringJdbc;
import guru.qa.niffler.data.repository.impl.SpendRepositoryHibernate;
import guru.qa.niffler.data.repository.impl.SpendRepositoryJdbc;
import guru.qa.niffler.data.repository.impl.SpendRepositorySpringJdbc;
import guru.qa.niffler.data.repository.impl.UserdataUserRepositoryHibernate;
import guru.qa.niffler.data.repository.impl.UserdataUserRepositoryJdbc;
import guru.qa.niffler.data.repository.impl.UserdataUserRepositorySpringJdbc;
import guru.qa.niffler.data.tpl.DataSources;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.SpendDbClient;
import guru.qa.niffler.service.UsersDbClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Date;
import java.util.List;

import static guru.qa.niffler.utils.RandomDataUtils.randomPassword;
import static guru.qa.niffler.utils.RandomDataUtils.randomUsername;

public class JdbcTest {

    @Test
    void txTest() {
        SpendDbClient spendDbClient = new SpendDbClient();

        SpendJson spend = spendDbClient.createSpend(
                new SpendJson(
                        null,
                        new Date(),
                        new CategoryJson(
                                null,
                                "cat-name-tx-3",
                                "duck",
                                false
                        ),
                        CurrencyValues.RUB,
                        1000.0,
                        "spend-name-tx-3",
                        "duck"
                )
        );

        System.out.println(spend);
    }

    static UsersDbClient usersDbClient = new UsersDbClient();

    @ValueSource(strings = {
            "valentin-10"
    })
    @ParameterizedTest
    void springJdbcTest(String uname) {

        UserJson user = usersDbClient.createUser(
                uname,
                "12345"
        );

        usersDbClient.addIncomeInvitation(user, 1);
        usersDbClient.addOutcomeInvitation(user, 1);
    }

    private static final Config CFG = Config.getInstance();

    @Test
    @DisplayName("CRUD для AuthUserRepositoryHibernate")
    void testAuthUserRepositoryHibernate() {
        new XaTransactionTemplate(
                CFG.authJdbcUrl()
        ).execute(() -> {
            AuthUserEntity authUserEntity = new AuthUserEntity();
            authUserEntity.setUsername(randomUsername());
            authUserEntity.setPassword(randomPassword());
            authUserEntity.setEnabled(true);
            authUserEntity.setAccountNonLocked(true);
            authUserEntity.setCredentialsNonExpired(true);
            authUserEntity.setAccountNonExpired(true);
            AuthUserRepositoryHibernate authUserRepositoryHibernate = new AuthUserRepositoryHibernate();
            authUserEntity = authUserRepositoryHibernate.create(authUserEntity);

            System.out.println(authUserEntity.getUsername());

            Assertions.assertTrue(authUserRepositoryHibernate.findById(authUserEntity.getId()).isPresent(),
                    "failed create AuthUserRepositoryHibernate");

            authUserEntity.setAccountNonExpired(false);
            authUserRepositoryHibernate.update(authUserEntity);

            Assertions.assertFalse(authUserRepositoryHibernate
                            .findById(authUserRepositoryHibernate
                                    .update(authUserEntity)
                                    .getId())
                            .get()
                            .getAccountNonExpired(),
                    "failed update AuthUserRepositoryHibernate");

            authUserRepositoryHibernate.remove(authUserEntity);

            Assertions.assertFalse(authUserRepositoryHibernate
                            .findById(authUserEntity.getId()).isPresent(),
                    "failed delete AuthUserRepositoryHibernate");
            return null;
        });
    }

    @Test
    @DisplayName("CRUD для AuthUserRepositoryJdbc")
    void testAuthUserRepositoryJdbc() {
        new TransactionTemplate(
                new JdbcTransactionManager(
                        DataSources.dataSource(CFG.authJdbcUrl())
                )
        ).execute(status -> {
            AuthUserEntity authUserEntity = new AuthUserEntity();
            authUserEntity.setUsername(randomUsername());
            authUserEntity.setPassword(randomPassword());
            authUserEntity.setEnabled(true);
            authUserEntity.setAccountNonLocked(true);
            authUserEntity.setCredentialsNonExpired(true);
            authUserEntity.setAccountNonExpired(true);
            AuthUserRepositoryJdbc authUserRepositoryJdbc = new AuthUserRepositoryJdbc();
            AuthAuthorityDaoJdbc authAuthorityDaoJdbc = new AuthAuthorityDaoJdbc();
            authUserEntity = authUserRepositoryJdbc.create(authUserEntity);
            authUserEntity.getAuthorities()
                    .addAll(List.of(
                                    authAuthorityDaoJdbc
                                            .create(new AuthorityEntity(Authority.write, authUserEntity)),
                                    authAuthorityDaoJdbc
                                            .create(new AuthorityEntity(Authority.read, authUserEntity))
                            )
                    );

            System.out.println(authUserEntity.getUsername());

            Assertions.assertTrue(authUserRepositoryJdbc.findById(authUserEntity.getId()).isPresent(),
                    "failed create or find AuthUserRepositoryJdbc");

            authUserEntity.setAccountNonExpired(false);
            authUserRepositoryJdbc.update(authUserEntity);

            Assertions.assertFalse(authUserRepositoryJdbc
                            .findById(authUserRepositoryJdbc
                                    .update(authUserEntity)
                                    .getId())
                            .get()
                            .getAccountNonExpired(),
                    "failed update AuthUserRepositoryJdbc");

            authUserRepositoryJdbc.remove(authUserEntity);

            Assertions.assertFalse(authUserRepositoryJdbc
                            .findById(authUserEntity.getId()).isPresent(),
                    "failed delete AuthUserRepositoryJdbc");
            return null;
        });
    }

    @Test
    @DisplayName("CRUD для AuthUserRepositoryJdbcSpring")
    void testAuthUserRepositoryJdbcSpring() {
        new XaTransactionTemplate(
                CFG.authJdbcUrl()
        ).execute(() -> {
            AuthUserEntity authUserEntity = new AuthUserEntity();
            authUserEntity.setUsername(randomUsername());
            authUserEntity.setPassword(randomPassword());
            authUserEntity.setEnabled(true);
            authUserEntity.setAccountNonLocked(true);
            authUserEntity.setCredentialsNonExpired(true);
            authUserEntity.setAccountNonExpired(true);
            AuthUserRepositorySpringJdbc authUserRepositorySpringJdbc = new AuthUserRepositorySpringJdbc();
            authUserEntity = authUserRepositorySpringJdbc.create(authUserEntity);

            System.out.println(authUserEntity.getUsername());

            Assertions.assertTrue(authUserRepositorySpringJdbc.findById(authUserEntity.getId()).isPresent(),
                    "failed create or find AuthUserRepositoryJdbc");

            authUserEntity.setAccountNonExpired(false);
            authUserRepositorySpringJdbc.update(authUserEntity);

            Assertions.assertFalse(authUserRepositorySpringJdbc
                            .findById(authUserRepositorySpringJdbc
                                    .update(authUserEntity)
                                    .getId())
                            .get()
                            .getAccountNonExpired(),
                    "failed update AuthUserRepositoryJdbc");

            authUserRepositorySpringJdbc.remove(authUserEntity);

            Assertions.assertFalse(authUserRepositorySpringJdbc
                            .findById(authUserEntity.getId()).isPresent(),
                    "failed delete AuthUserRepositoryJdbc");
            return null;
        });
    }

    @Test
    @DisplayName("CRUD для UserdataUserRepositoryHibernate")
    void testUserdataUserRepositoryHibernate() {
        new XaTransactionTemplate(
                CFG.authJdbcUrl()
        ).execute(() -> {
            UserdataUserEntity userdataUserEntity = new UserdataUserEntity();
            userdataUserEntity.setUsername(randomUsername());
            userdataUserEntity.setCurrency(CurrencyValues.RUB);
            UserdataUserRepositoryHibernate userdataUserRepositoryHibernate = new UserdataUserRepositoryHibernate();
            userdataUserEntity = userdataUserRepositoryHibernate.create(userdataUserEntity);

            System.out.println(userdataUserEntity.getUsername());

            Assertions.assertTrue(userdataUserRepositoryHibernate.findById(userdataUserEntity.getId()).isPresent(),
                    "failed create UserdataUserRepositoryHibernate");

            userdataUserEntity.setFirstname("firtsname");
            userdataUserRepositoryHibernate.update(userdataUserEntity);

            Assertions.assertEquals("firtsname", userdataUserRepositoryHibernate
                            .findById(userdataUserRepositoryHibernate
                                    .update(userdataUserEntity)
                                    .getId())
                            .get()
                            .getFirstname(),
                    "failed update UserdataUserRepositoryHibernate");

            userdataUserRepositoryHibernate.remove(userdataUserEntity);

            Assertions.assertFalse(userdataUserRepositoryHibernate
                            .findById(userdataUserEntity.getId()).isPresent(),
                    "failed delete UserdataUserRepositoryHibernate");
            return null;
        });
    }

    @Test
    @DisplayName("CRUD для UserdataUserRepositoryJdbc")
    void testUserdataUserRepositoryJdbc() {
        new TransactionTemplate(
                new JdbcTransactionManager(
                        DataSources.dataSource(CFG.authJdbcUrl())
                )
        ).execute(status -> {
            UserdataUserEntity userdataUserEntity = new UserdataUserEntity();
            userdataUserEntity.setUsername(randomUsername());
            userdataUserEntity.setCurrency(CurrencyValues.RUB);
            UserdataUserRepositoryJdbc userdataUserRepositoryJdbc = new UserdataUserRepositoryJdbc();
            userdataUserEntity = userdataUserRepositoryJdbc.create(userdataUserEntity);

            System.out.println(userdataUserEntity.getUsername());

            Assertions.assertTrue(userdataUserRepositoryJdbc.findById(userdataUserEntity.getId()).isPresent(),
                    "failed create UserdataUserRepositoryHibernate");

            userdataUserEntity.setFirstname("firtsname");
            userdataUserRepositoryJdbc.update(userdataUserEntity);

            Assertions.assertEquals("firtsname", userdataUserRepositoryJdbc
                            .findById(userdataUserRepositoryJdbc
                                    .update(userdataUserEntity)
                                    .getId())
                            .get()
                            .getFirstname(),
                    "failed update UserdataUserRepositoryHibernate");

            userdataUserRepositoryJdbc.remove(userdataUserEntity);

            Assertions.assertFalse(userdataUserRepositoryJdbc
                            .findById(userdataUserEntity.getId()).isPresent(),
                    "failed delete UserdataUserRepositoryHibernate");
            return null;
        });
    }

    @Test
    @DisplayName("CRUD для UserdataUserRepositoryJdbcSpring")
    void testUserdataUserRepositoryJdbcSpring() {
        new XaTransactionTemplate(
                CFG.authJdbcUrl()
        ).execute(() -> {
            UserdataUserEntity userdataUserEntity = new UserdataUserEntity();
            userdataUserEntity.setUsername(randomUsername());
            userdataUserEntity.setCurrency(CurrencyValues.RUB);
            UserdataUserRepositorySpringJdbc userdataUserRepositorySpringJdbc = new UserdataUserRepositorySpringJdbc();
            userdataUserEntity = userdataUserRepositorySpringJdbc.create(userdataUserEntity);

            System.out.println(userdataUserEntity.getUsername());

            Assertions.assertTrue(userdataUserRepositorySpringJdbc.findById(userdataUserEntity.getId()).isPresent(),
                    "failed create UserdataUserRepositoryHibernate");

            userdataUserEntity.setFirstname("firtsname");
            userdataUserRepositorySpringJdbc.update(userdataUserEntity);

            Assertions.assertEquals("firtsname", userdataUserRepositorySpringJdbc
                            .findById(userdataUserRepositorySpringJdbc
                                    .update(userdataUserEntity)
                                    .getId())
                            .get()
                            .getFirstname(),
                    "failed update UserdataUserRepositoryHibernate");

            userdataUserRepositorySpringJdbc.remove(userdataUserEntity);

            Assertions.assertFalse(userdataUserRepositorySpringJdbc
                            .findById(userdataUserEntity.getId()).isPresent(),
                    "failed delete UserdataUserRepositoryHibernate");
            return null;
        });
    }

    @Test
    @DisplayName("CRUD для SpendRepositoryHibernate")
    void testSpendRepositoryHibernate() {
        new XaTransactionTemplate(
                CFG.authJdbcUrl()
        ).execute(() -> {
            String username = randomUsername();
            SpendEntity spendEntity = new SpendEntity();
            spendEntity.setSpendDate(new java.sql.Date(new Date().getTime()));
            spendEntity.setCurrency(CurrencyValues.RUB);
            spendEntity.setAmount(1200.0);
            spendEntity.setDescription("test");
            spendEntity.setUsername(username);
            CategoryEntity categoryEntity = new CategoryEntity();
            categoryEntity.setName("a");
            categoryEntity.setUsername(username);
            spendEntity.setCategory(categoryEntity);

            SpendRepositoryHibernate spendRepositoryHibernate = new SpendRepositoryHibernate();
            spendEntity = spendRepositoryHibernate.create(spendEntity);

            System.out.println(spendEntity.getCategory().getName());

            Assertions.assertTrue(spendRepositoryHibernate.findById(spendEntity.getId()).isPresent(),
                    "failed create SpendRepositoryHibernate");

            spendEntity.setDescription("new description");
            spendRepositoryHibernate.update(spendEntity);

            Assertions.assertEquals("new description", spendRepositoryHibernate
                            .findById(spendRepositoryHibernate
                                    .update(spendEntity)
                                    .getId())
                            .get()
                            .getDescription(),
                    "failed update SpendRepositoryHibernate");

            spendRepositoryHibernate.remove(spendEntity);

            Assertions.assertFalse(spendRepositoryHibernate
                            .findById(spendEntity.getId()).isPresent(),
                    "failed delete SpendRepositoryHibernate");

            return null;
        });
    }

    @Test
    @DisplayName("CRUD для SpendRepositoryJdbc")
    void testSpendRepositoryJdbc() {
        new TransactionTemplate(
                new JdbcTransactionManager(
                        DataSources.dataSource(CFG.authJdbcUrl())
                )
        ).execute(status -> {
            String username = randomUsername();
            SpendEntity spendEntity = new SpendEntity();
            spendEntity.setSpendDate(new java.sql.Date(new Date().getTime()));
            spendEntity.setCurrency(CurrencyValues.RUB);
            spendEntity.setAmount(1200.0);
            spendEntity.setDescription("test");
            spendEntity.setUsername(username);
            CategoryEntity categoryEntity = new CategoryEntity();
            categoryEntity.setName("a");
            categoryEntity.setUsername(username);
            spendEntity.setCategory(categoryEntity);

            SpendRepositoryJdbc spendRepositoryJdbc = new SpendRepositoryJdbc();
            spendEntity = spendRepositoryJdbc.create(spendEntity);

            System.out.println(spendEntity.getCategory().getName());

            Assertions.assertTrue(spendRepositoryJdbc.findById(spendEntity.getId()).isPresent(),
                    "failed create SpendRepositoryJdbc");

            spendEntity.setDescription("new description");
            spendRepositoryJdbc.update(spendEntity);

            Assertions.assertEquals("new description", spendRepositoryJdbc
                            .findById(spendRepositoryJdbc
                                    .update(spendEntity)
                                    .getId())
                            .get()
                            .getDescription(),
                    "failed update SpendRepositoryJdbc");

            spendRepositoryJdbc.remove(spendEntity);

            Assertions.assertFalse(spendRepositoryJdbc
                            .findById(spendEntity.getId()).isPresent(),
                    "failed delete SpendRepositoryJdbc");

            return null;
        });
    }

    @Test
    @DisplayName("CRUD для SpendRepositoryJdbcSpring")
    void testSpendRepositoryJdbcSpring() {
        new XaTransactionTemplate(
                CFG.authJdbcUrl()
        ).execute(() -> {
            String username = randomUsername();
            SpendEntity spendEntity = new SpendEntity();
            spendEntity.setSpendDate(new java.sql.Date(new Date().getTime()));
            spendEntity.setCurrency(CurrencyValues.RUB);
            spendEntity.setAmount(1200.0);
            spendEntity.setDescription("test");
            spendEntity.setUsername(username);
            CategoryEntity categoryEntity = new CategoryEntity();
            categoryEntity.setName("a");
            categoryEntity.setUsername(username);
            spendEntity.setCategory(categoryEntity);

            SpendRepositorySpringJdbc spendRepositorySpringJdbc = new SpendRepositorySpringJdbc();
            spendEntity = spendRepositorySpringJdbc.create(spendEntity);

            System.out.println(spendEntity.getCategory().getName());

            Assertions.assertTrue(spendRepositorySpringJdbc.findById(spendEntity.getId()).isPresent(),
                    "failed create SpendRepositoryJdbc");

            spendEntity.setDescription("new description");
            spendRepositorySpringJdbc.update(spendEntity);

            Assertions.assertEquals("new description", spendRepositorySpringJdbc
                            .findById(spendRepositorySpringJdbc
                                    .update(spendEntity)
                                    .getId())
                            .get()
                            .getDescription(),
                    "failed update SpendRepositoryJdbc");

            spendRepositorySpringJdbc.remove(spendEntity);

            Assertions.assertFalse(spendRepositorySpringJdbc
                            .findById(spendEntity.getId()).isPresent(),
                    "failed delete SpendRepositoryJdbc");

            return null;
        });
    }
}