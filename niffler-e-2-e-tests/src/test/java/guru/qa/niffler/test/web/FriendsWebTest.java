package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.meta.User;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

public class FriendsWebTest extends BaseWebTest {

    @User(friends = 1)
    @Test
    void friendShouldBePresentInFriendsTable( UserJson user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .clickOnMenuButton()
                .clickOnFriendsButton()
                .checkThatFriendsTableContainsFriend(user.testData().friendsUsernames()[0])
                .checkThatUnfriendButtonVisible(user.testData().friendsUsernames()[0]);
    }

    @User
    @Test
    void friendTableShouldBeEmptyForNewUser(UserJson user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .clickOnMenuButton()
                .clickOnFriendsButton()
                .checkThatTableEmpty();
    }


    @User(incomeInvitations = 1)
    @Test
    void incomeInvitationBePresentInFriendsTable(UserJson user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .clickOnMenuButton()
                .clickOnFriendsButton()
                .checkThatRequestsTableContainsRequest(user.testData().incomeInvitationsUsernames()[0])
                .checkThatAcceptButtonVisible(user.testData().incomeInvitationsUsernames()[0])
                .checkThatDeclineButtonVisible(user.testData().incomeInvitationsUsernames()[0]);
    }


    @User(outcomeInvitations = 1)
    @Test
    void outcomeInvitationBePresentInAllPeopleTable(UserJson user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .clickOnMenuButton()
                .clickOnFriendsButton()
                .checkThatTableEmpty()
                .clickOnAllFriendsButton()
                .checkThatOutcomeRequestVisible(user.testData().outcomeInvitationsUsernames()[0]);
    }
}
