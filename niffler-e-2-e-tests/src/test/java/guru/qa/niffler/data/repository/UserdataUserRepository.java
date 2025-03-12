package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.entity.userdata.UserdataUserEntity;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;
import java.util.UUID;

@ParametersAreNonnullByDefault
public interface UserdataUserRepository {

    @Nonnull
    UserdataUserEntity create(UserdataUserEntity user);

    @Nonnull
    Optional<UserdataUserEntity> findById(UUID id);

    @Nonnull
    Optional<UserdataUserEntity> findByUsername(String username);

    @Nonnull
    UserdataUserEntity update(UserdataUserEntity user);

    void addIncomeInvitation(UserdataUserEntity requester, UserdataUserEntity addressee);

    void addOutcomeInvitation(UserdataUserEntity requester, UserdataUserEntity addressee);

    void addFriend(UserdataUserEntity requester, UserdataUserEntity addressee);

    void remove(UserdataUserEntity user);
}
