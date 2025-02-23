package guru.qa.niffler.test.web;

import guru.qa.niffler.data.entity.userdata.UserdataUserEntity;
import guru.qa.niffler.data.repository.impl.UserdataUserRepositorySpringJdbc;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.SpendDbClient;
import guru.qa.niffler.service.UsersDbClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Date;
import java.util.UUID;

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

    @Test
    void test() {
        UserdataUserRepositorySpringJdbc userdataUserRepositorySpringJdbc = new UserdataUserRepositorySpringJdbc();
        UserdataUserEntity userdataUserEntity = userdataUserRepositorySpringJdbc
                .findByUserId(UUID.fromString("0d52a139-d4ba-4ed8-99a3-4ff0dd3b8fb0")).get();


        System.out.println(userdataUserEntity);
    }
}