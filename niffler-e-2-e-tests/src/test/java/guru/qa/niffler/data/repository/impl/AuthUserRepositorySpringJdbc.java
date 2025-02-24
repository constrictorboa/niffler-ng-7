package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.dao.impl.AuthAuthorityDaoSpringJdbc;
import guru.qa.niffler.data.dao.impl.AuthUserDaoSpringJdbc;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.extractor.AuthUserEntityExtractor;
import guru.qa.niffler.data.repository.AuthUserRepository;
import guru.qa.niffler.data.tpl.DataSources;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public class AuthUserRepositorySpringJdbc implements AuthUserRepository {

    private static final Config CFG = Config.getInstance();
    private final AuthUserDao authUserDao = new AuthUserDaoSpringJdbc();
    private final AuthAuthorityDao authAuthorityDao = new AuthAuthorityDaoSpringJdbc();

    @Override
    public Optional<AuthUserEntity> findById(UUID id) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.authJdbcUrl()));
        return Optional.ofNullable(jdbcTemplate.query("SELECT a.id as authority_id, authority, user_id as id, u.username, u.password, " +
                "u.enabled, u.account_non_expired, u.account_non_locked, u.credentials_non_expired from \"user\" u left join authority " +
                "a on u.id = a.user_id where u.id = '" + id + "'", AuthUserEntityExtractor.instance));

    }

    @Override
    public AuthUserEntity create(AuthUserEntity user) {
        AuthUserEntity createdUser = authUserDao.create(user);
        createdUser
                .getAuthorities()
                .addAll(List.of(
                                authAuthorityDao
                                        .create(new AuthorityEntity(Authority.write, createdUser)),
                                authAuthorityDao
                                        .create(new AuthorityEntity(Authority.read, createdUser))
                        )
                );
        return createdUser;
    }

    @Override
    public Optional<AuthUserEntity> findByUsername(String username) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.authJdbcUrl()));
        return Optional.ofNullable(jdbcTemplate.query("SELECT a.id as authority_id, authority, user_id as id, u.username, u.password, " +
                "u.enabled, u.account_non_expired, u.account_non_locked , u.account_non_expired from \"user\" u join authority " +
                "a on u.id = a.user_id where u.username = '" + username + "'", AuthUserEntityExtractor.instance));

    }

    @Override
    public AuthUserEntity update(AuthUserEntity user) {
        for (AuthorityEntity ae : user.getAuthorities()) {
            authAuthorityDao.update(ae);
        }
        authUserDao.update(user);
        return user;
    }

    @Override
    public void remove(AuthUserEntity user) {
        authAuthorityDao.deleteByUserId(user.getId());
        authUserDao.deleteById(user);
    }
}
