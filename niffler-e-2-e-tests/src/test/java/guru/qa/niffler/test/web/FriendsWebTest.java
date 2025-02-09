package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension.StaticUser;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType.Type.WITH_FRIEND;
import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType.Type.WITH_INCOME_REQUEST;
import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType.Type.EMPTY;
import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType.Type.WITH_OUTCOME_REQUEST;

public class FriendsWebTest extends BaseWebTest {
    @Test
    @ExtendWith(UsersQueueExtension.class)
    void friendShouldBePresentInFriendsTable(@UserType(WITH_FRIEND) StaticUser user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.password())
                .menuButtonClick()
                .friendsButtonClick()
                .checkThatFriendsTableContainsFriend(user.friend())
                .checkThatUnfriendButtonVisible(user.friend());
    }

    @Test
    @ExtendWith(UsersQueueExtension.class)
    void friendTableShouldBeEmptyForNewUser(@UserType(EMPTY) StaticUser user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.password())
                .menuButtonClick()
                .friendsButtonClick()
                .checkThatTableEmpty();
    }


    @Test
    @ExtendWith(UsersQueueExtension.class)
    void incomeInvitationBePresentInFriendsTable(@UserType(WITH_INCOME_REQUEST) StaticUser user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.password())
                .menuButtonClick()
                .friendsButtonClick()
                .checkThatRequestsTableContainsRequest(user.income())
                .checkThatAcceptButtonVisible(user.income())
                .checkThatDeclineButtonVisible(user.income());
    }


    @Test
    @ExtendWith(UsersQueueExtension.class)
    void outcomeInvitationBePresentInAllPeopleTable(@UserType(WITH_OUTCOME_REQUEST) StaticUser user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.password())
                .menuButtonClick()
                .friendsButtonClick()
                .checkThatTableEmpty()
                .allFriendsButtonClick()
                .checkThatOutcomeRequestVisible(user.outcome());
    }
}
