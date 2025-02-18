package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AuthAuthorityDaoJdbc implements AuthAuthorityDao {
    private static final Config CFG = Config.getInstance();
    private final Connection connection;

    public AuthAuthorityDaoJdbc(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void create(AuthorityEntity... authority) {
        for (AuthorityEntity ae : authority) {
            create(ae);
        }
    }

    @Override
    public AuthorityEntity create(AuthorityEntity authority) {
        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO authority (user_id, authority) " +
                        "VALUES (?, ?)",
                Statement.RETURN_GENERATED_KEYS
        )) {
            ps.setObject(1, authority.getUser().getId());
            ps.setString(2, authority.getAuthority().name());
            ps.executeUpdate();

            final UUID generatedKey;
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    generatedKey = rs.getObject("id", UUID.class);
                } else {
                    throw new SQLException("Can`t find id in ResultSet");
                }
            }
            authority.setId(generatedKey);
            return authority;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<AuthorityEntity> findAll() {
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT * FROM authority"
        )) {
            List<AuthorityEntity> authorityEntityList = new ArrayList<>();
            ps.execute();
            try (ResultSet rs = ps.getResultSet()) {
                if (rs.next()) {
                    authorityEntityList.add(extractAuthorityEntityFromResultSet(rs));
                }
            }
            return authorityEntityList;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private AuthorityEntity extractAuthorityEntityFromResultSet(ResultSet rs) throws SQLException {
        AuthorityEntity authorityEntity = new AuthorityEntity();
        authorityEntity.setId(rs.getObject("id", UUID.class));
        authorityEntity.setAuthority(Authority.valueOf(rs.getString("authority")));
        authorityEntity.setUser(new AuthUserEntity());
        authorityEntity.getUser().setId(rs.getObject("user_id", UUID.class));
        return authorityEntity;
    }
}
