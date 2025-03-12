package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.UserdataUserDao;
import guru.qa.niffler.data.dao.impl.UserdataUserDaoSpringJdbc;
import guru.qa.niffler.data.entity.userdata.FriendshipStatus;
import guru.qa.niffler.data.entity.userdata.UserdataUserEntity;
import guru.qa.niffler.data.extractor.UserdataUserEntityExtractor;
import guru.qa.niffler.data.repository.UserdataUserRepository;
import guru.qa.niffler.data.jdbc.DataSources;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;
import java.util.UUID;

@ParametersAreNonnullByDefault
public class UserdataUserRepositorySpringJdbc implements UserdataUserRepository {
    private static final Config CFG = Config.getInstance();

    private final UserdataUserDao userdataUserDao = new UserdataUserDaoSpringJdbc();

    @Nonnull
    @Override
    public Optional<UserdataUserEntity> findById(UUID id) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.userdataJdbcUrl()));
        return Optional.ofNullable(jdbcTemplate.query("SELECT f.requester_id, f.addressee_id, f.status, f.created_date, u.id, u.username, " +
                "u.currency, u.firstname, u.surname, u.photo, u.photo_small, u.full_name from \"user\" u left join friendship  " +
                "f on u.id = f.requester_id or u.id = f.addressee_id where u.id ='" + id + "'", UserdataUserEntityExtractor.instance));

    }

    @Nonnull
    @Override
    public UserdataUserEntity create(UserdataUserEntity user) {
        return userdataUserDao.create(user);
    }

    @Nonnull
    @Override
    public Optional<UserdataUserEntity> findByUsername(String username) {
        return userdataUserDao.findByUsername(username);
    }


    @Nonnull
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
