package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.extractor.AuthUserEntityExtractor;
import guru.qa.niffler.data.repository.AuthUserSpringRepository;
import guru.qa.niffler.data.tpl.DataSources;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Optional;
import java.util.UUID;


public class AuthUserRepositorySpringJdbc implements AuthUserSpringRepository {

    private static final Config CFG = Config.getInstance();

    @Override
    public Optional<AuthUserEntity> findByUserId(UUID id) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.authJdbcUrl()));
        return Optional.ofNullable(jdbcTemplate.query("SELECT a.id as authority_id, authority, user_id as id, u.username, u.password, " +
                "u.enabled, u.account_non_expired, u.account_non_locked , u.account_non_expired from \"user\" u join authority " +
                "a on u.id = a.user_id where u.id = '" + id + "'", AuthUserEntityExtractor.instance));

    }

    @Override
    public Optional<AuthUserEntity> findByUsername(String username) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.authJdbcUrl()));
        return Optional.ofNullable(jdbcTemplate.query("SELECT a.id as authority_id, authority, user_id as id, u.username, u.password, " +
                "u.enabled, u.account_non_expired, u.account_non_locked , u.account_non_expired from \"user\" u join authority " +
                "a on u.id = a.user_id where u.username = '" + username + "'", AuthUserEntityExtractor.instance));

    }
}
