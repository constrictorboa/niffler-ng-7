package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.UserdataUserDao;
import guru.qa.niffler.data.dao.impl.UserdataUserDAOJdbc;
import guru.qa.niffler.data.entity.userdata.FriendshipStatus;
import guru.qa.niffler.data.entity.userdata.UserdataUserEntity;
import guru.qa.niffler.data.repository.UserdataUserRepository;

import java.util.Optional;
import java.util.UUID;

public class UserdataUserRepositoryJdbc implements UserdataUserRepository {
    private static final Config CFG = Config.getInstance();

    private final UserdataUserDao userdataUserDao = new UserdataUserDAOJdbc();

    @Override
    public UserdataUserEntity create(UserdataUserEntity user) {
        return userdataUserDao.create(user);
    }

    @Override
    public Optional<UserdataUserEntity> findById(UUID id) {
        return userdataUserDao.findById(id);
    }

    @Override
    public Optional<UserdataUserEntity> findByUsername(String username) {
        return userdataUserDao.findByUsername(username);
    }

    @Override
    public UserdataUserEntity update(UserdataUserEntity user) {
        return userdataUserDao.update(user);
    }

    @Override
    public void addIncomeInvitation(UserdataUserEntity requester, UserdataUserEntity addressee) {
        requester.addFriends(FriendshipStatus.PENDING, addressee);
        userdataUserDao.update(requester);

    }

    @Override
    public void addOutcomeInvitation(UserdataUserEntity requester, UserdataUserEntity addressee) {
        addressee.addFriends(FriendshipStatus.PENDING, requester);
        userdataUserDao.update(addressee);

    }

    @Override
    public void addFriend(UserdataUserEntity requester, UserdataUserEntity addressee) {
        requester.addFriends(FriendshipStatus.ACCEPTED, addressee);
        addressee.addFriends(FriendshipStatus.ACCEPTED, requester);
        userdataUserDao.update(requester);
        userdataUserDao.update(addressee);
    }

    @Override
    public void remove(UserdataUserEntity user) {
        userdataUserDao.deleteUser(user);
    }
}
