package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.meta.User;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

public class FriendsWebTest extends BaseWebTest {

    @User(friends = 1)
    @Test
    void friendShouldBePresentInFriendsTable(UserJson user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .goToFriendsPage()
                .checkThatFriendsTableContainsFriend(user.getFirstFriendsUsername())
                .checkThatUnfriendButtonVisible(user.getFirstFriendsUsername());
    }

    @User
    @Test
    void friendTableShouldBeEmptyForNewUser(UserJson user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .goToFriendsPage()
                .checkThatTableEmpty();
    }


    @User(incomeInvitations = 1)
    @Test
    void incomeInvitationBePresentInFriendsTable(UserJson user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .goToFriendsPage()
                .checkThatRequestsTableContainsRequest(user.testData().incomeInvitationsUsernames()[0])
                .checkThatAcceptButtonVisible(user.testData().incomeInvitationsUsernames()[0])
                .checkThatDeclineButtonVisible(user.testData().incomeInvitationsUsernames()[0]);
    }


    @User(outcomeInvitations = 1)
    @Test
    void outcomeInvitationBePresentInAllPeopleTable(UserJson user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .goToFriendsPage()
                .checkThatTableEmpty()
                .clickOnAllFriendsButton()
                .checkThatOutcomeRequestVisible(user.testData().outcomeInvitationsUsernames()[0]);
    }

    @User(incomeInvitations = 1)
    @Test
    void friendShouldBeAccept(UserJson user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .goToFriendsPage()
                .clickAcceptButton(user.testData().incomeInvitationsUsernames()[0])
                .checkAlertMessage("Invitation of " + user.testData().incomeInvitationsUsernames()[0] + " accepted")
                .checkThatFriendsTableContainsFriend(user.testData().incomeInvitationsUsernames()[0])
                .checkThatUnfriendButtonVisible(user.testData().incomeInvitationsUsernames()[0]);
    }

    @User(incomeInvitations = 1)
    @Test
    void friendShouldBeDecline(UserJson user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .goToFriendsPage()
                .clickDeclineButton(user.testData().incomeInvitationsUsernames()[0])
                .checkAlertMessage("Invitation of " + user.testData().incomeInvitationsUsernames()[0] + " is declined")
                .checkThatTableEmpty();
    }
}
