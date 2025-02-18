package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.dao.UserdataUserDao;
import guru.qa.niffler.data.dao.impl.AuthAuthorityDaoSpringJdbc;
import guru.qa.niffler.data.dao.impl.AuthUserDaoSpringJdbc;
import guru.qa.niffler.data.dao.impl.UserdataUserDaoSpringJdbc;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.userdata.UserdataUserEntity;
import guru.qa.niffler.data.tpl.DataSources;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.UserJson;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Arrays;

public class UsersDbClient {
    private static final Config CFG = Config.getInstance();
    private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    private final AuthUserDao authUserDao = new AuthUserDaoSpringJdbc();
    private final AuthAuthorityDao authAuthorityDao = new AuthAuthorityDaoSpringJdbc();
    private final UserdataUserDao userdataUserDao = new UserdataUserDaoSpringJdbc();

    private final TransactionTemplate txTemplate = new TransactionTemplate(
            new JdbcTransactionManager(
                    DataSources.dataSource(CFG.authJdbcUrl())
            )
    );

    private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
            CFG.authJdbcUrl(),
            CFG.userdataJdbcUrl()
    );

    public UserJson createUser(UserJson user) {
        return xaTransactionTemplate.execute(() -> {
                    AuthUserEntity authUser = new AuthUserEntity();
                    authUser.setUsername(user.username());
                    authUser.setPassword(pe.encode("12345"));
                    authUser.setEnabled(true);
                    authUser.setAccountNonExpired(true);
                    authUser.setAccountNonLocked(true);
                    authUser.setCredentialsNonExpired(true);

                    AuthUserEntity createdAuthUser = authUserDao.create(authUser);

                    AuthorityEntity[] authorityEntities = Arrays.stream(Authority.values()).map(
                            e -> {
                                AuthorityEntity ae = new AuthorityEntity();
                                ae.setId(createdAuthUser.getId());
                                ae.setAuthority(e);
                                return ae;
                            }
                    ).toArray(AuthorityEntity[]::new);

                    authAuthorityDao.create(authorityEntities);
                    return UserJson.fromEntity(
                            userdataUserDao.create(UserdataUserEntity.fromJson(user)),
                            null
                    );
                }
        );
    }

//    public UserJson createUserSpringJdbc(UserJson user) {
//        AuthUserEntity authUser = new AuthUserEntity();
//        authUser.setUsername(user.username());
//        authUser.setPassword(pe.encode("12345"));
//        authUser.setEnabled(true);
//        authUser.setAccountNonExpired(true);
//        authUser.setAccountNonLocked(true);
//        authUser.setCredentialsNonExpired(true);
//
//        AuthUserEntity createdAuthUser = new AuthUserDaoSpringJdbc(dataSource(CFG.authJdbcUrl()))
//                .create(authUser);
//
//        AuthorityEntity[] authorityEntities = Arrays.stream(Authority.values()).map(
//                e -> {
//                    AuthorityEntity ae = new AuthorityEntity();
//                    ae.setId(createdAuthUser.getId());
//                    ae.setAuthority(e);
//                    return ae;
//                }
//        ).toArray(AuthorityEntity[]::new);
//
//        new AuthAuthorityDaoSpringJdbc(dataSource(CFG.authJdbcUrl()))
//                .create(authorityEntities);
//
//        return UserJson.fromEntity(
//                new UserdataUserDaoSpringJdbc(dataSource(CFG.userdataJdbcUrl()))
//                        .create(
//                                UserdataUserEntity.fromJson(user)
//                        ),
//                null
//        );
//    }
//
//
//    public UserJson createUser(UserJson userJson) {
//        return UserJson.fromEntity( xaTransaction(1, new Databases.XaFunction<>(connection -> {
//                    AuthUserEntity userEntity = new AuthUserEntity();
//                    userEntity.setUsername(userJson.username());
//                    userEntity.setPassword("12345");
//                    userEntity.setEnabled(true);
//                    userEntity.setAccountNonExpired(true);
//                    userEntity.setAccountNonLocked(true);
//                    userEntity.setCredentialsNonExpired(true);
//                    userEntity = new AuthUserDaoJdbc(connection).create(userEntity);
//                    AuthAuthorityDaoJdbc authAuthorityDaoJdbc = new AuthAuthorityDaoJdbc(connection);
//                    userEntity.addAuthorities(authAuthorityDaoJdbc.create(new AuthorityEntity(Authority.write, userEntity)));
//                    userEntity.addAuthorities(authAuthorityDaoJdbc.create(new AuthorityEntity(Authority.read, userEntity)));
//                    return null;
//                }, CFG.authJdbcUrl()),
//                new Databases.XaFunction<>(connection -> new UserdataUserDAOJdbc(connection)
//                        .create(UserdataUserEntity.fromJson(userJson)), CFG.userdataJdbcUrl())
//        ));
//    }
}
