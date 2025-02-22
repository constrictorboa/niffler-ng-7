package guru.qa.niffler.test.web;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.SpendDbClient;
import guru.qa.niffler.service.UsersDbClient;
import org.junit.jupiter.api.Test;

import java.util.Date;

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

    @Test
    void springJdbcTestWithTransaction() {
        UsersDbClient usersDbClient = new UsersDbClient();

        UserJson user = usersDbClient.createUser(
                new UserJson(
                        null,
                        "springJdbcTestWithTransaction-1",
                        "firtsname",
                        "surname",
                        "fulname",
                        CurrencyValues.RUB,
                        null,
                        null,
                        null
                )
        );

        System.out.println(user);
    }

    @Test
    void springJdbcTestWithoutTransaction() {
        UsersDbClient usersDbClient = new UsersDbClient();

        UserJson user = usersDbClient.createUserSpringWithout(
                new UserJson(
                        null,
                        "createUserSpringWithout-1",
                        "firtsname",
                        "surname",
                        "fulname",
                        CurrencyValues.RUB,
                        null,
                        null,
                        null
                )
        );

        System.out.println(user);
    }

    @Test
    void chainedTransactionTest() {
        UsersDbClient usersDbClient = new UsersDbClient();

        UserJson user = usersDbClient.createUserChainedTransaction(
                new UserJson(
                        null,
                        "user-test-chained-1",
                        null,
                        null,
                        null,
                        CurrencyValues.RUB,
                        null,
                        null,
                        null
                )
        );

        System.out.println(user);
    }

    @Test
    void jdbcTestWithTransaction() {
        UsersDbClient usersDbClient = new UsersDbClient();

        UserJson user = usersDbClient.createUserJdbcWithTransaction(
                new UserJson(
                        null,
                        "createUserJdbcWithTransaction-1",
                        "firtsname",
                        "surname",
                        "fulname",
                        CurrencyValues.RUB,
                        null,
                        null,
                        null
                )
        );

        System.out.println(user);
    }

    @Test
    void jdbcTestWithoutTransaction() {
        UsersDbClient usersDbClient = new UsersDbClient();

        UserJson user = usersDbClient.createUserJdbcWithoutTransaction(
                new UserJson(
                        null,
                        "createUserJdbcWithoutTransaction-1",
                        "firtsname",
                        "surname",
                        "fulname",
                        CurrencyValues.RUB,
                        null,
                        null,
                        null
                )
        );

        System.out.println(user);
    }
}