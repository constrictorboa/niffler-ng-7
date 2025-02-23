package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.entity.auth.AuthUserEntity;

import java.util.Optional;
import java.util.UUID;

public interface AuthUserSpringRepository {

    Optional<AuthUserEntity> findByUserId(UUID id);

    Optional<AuthUserEntity> findByUsername(String username);
}
