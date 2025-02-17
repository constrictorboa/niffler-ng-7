package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.mapper.AuthorityEntityRowMapper;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.UUID;

public class AuthAuthorityDaoSpringJdbc implements AuthAuthorityDao {

    private final DataSource dataSource;

    public AuthAuthorityDaoSpringJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void create(AuthorityEntity... authority) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.batchUpdate(
                "INSERT INTO authority (user_id, authority) VALUES (? , ?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setObject(1, authority[i].getId());
                        ps.setString(2, authority[i].getAuthority().name());
                    }

                    @Override
                    public int getBatchSize() {
                        return authority.length;
                    }
                }
        );
    }

    @Override
    public AuthorityEntity create(AuthorityEntity authority) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        KeyHolder kh = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO authority (user_id, authority) VALUES (? , ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            ps.setObject(1, authority.getUser().getId());
            ps.setString(2, authority.getAuthority().name());
            return ps;
        }, kh);

        final UUID generatedKey = (UUID) kh.getKeys().get("id");
        authority.setId(generatedKey);
        return authority;
    }

    @Override
    public List<AuthorityEntity> findAll() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        return jdbcTemplate.query("SELECT * FROM authority",
                AuthorityEntityRowMapper.instance);
    }
}