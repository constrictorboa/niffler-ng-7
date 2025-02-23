package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.entity.userdata.UserdataUserEntity;

import java.util.Optional;
import java.util.UUID;

public interface UserdataUserRepositorySpring {
    Optional<UserdataUserEntity> findByUserId(UUID id);
}
