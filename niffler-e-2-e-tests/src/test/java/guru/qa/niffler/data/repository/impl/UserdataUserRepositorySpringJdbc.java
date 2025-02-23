package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.userdata.UserdataUserEntity;
import guru.qa.niffler.data.extractor.UserdataUserEntityExtractor;
import guru.qa.niffler.data.repository.UserdataUserRepositorySpring;
import guru.qa.niffler.data.tpl.DataSources;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Optional;
import java.util.UUID;

public class UserdataUserRepositorySpringJdbc implements UserdataUserRepositorySpring {
    private static final Config CFG = Config.getInstance();

    @Override
    public Optional<UserdataUserEntity> findByUserId(UUID id) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.userdataJdbcUrl()));
        return Optional.ofNullable(jdbcTemplate.query("SELECT f.requester_id, f.addressee_id, f.status, f.created_date, u.id, u.username, " +
                "u.currency, u.firstname, u.surname, u.photo, u.photo_small, u.full_name from \"user\" u left join friendship  " +
                "f on u.id = f.requester_id or u.id = f.addressee_id where u.id ='" + id + "'", UserdataUserEntityExtractor.instance));

    }
}
