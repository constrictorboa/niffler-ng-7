package guru.qa.niffler.data.extractor;

import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class AuthUserEntityExtractor implements ResultSetExtractor<AuthUserEntity> {
    public static final AuthUserEntityExtractor instance = new AuthUserEntityExtractor();

    private AuthUserEntityExtractor() {
    }

    @Override
    public AuthUserEntity extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<UUID, AuthUserEntity> userMap = new ConcurrentHashMap<>();
        UUID userId = null;
        while (rs.next()) {
            userId = rs.getObject("id", UUID.class);
            AuthUserEntity user = userMap.computeIfAbsent(userId, id -> {
                AuthUserEntity authUser = new AuthUserEntity();
                authUser.setId(id);
                try {
                    authUser.setUsername(rs.getString("username"));
                    authUser.setPassword(rs.getString("password"));
                    authUser.setEnabled(rs.getBoolean("enabled"));
                    authUser.setAccountNonExpired(rs.getBoolean("account_non_expired"));
                    authUser.setAccountNonLocked(rs.getBoolean("account_non_locked"));
                    authUser.setCredentialsNonExpired(rs.getBoolean("credentials_non_expired"));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                return authUser;
            });

            AuthorityEntity authority = new AuthorityEntity();
            authority.setId(rs.getObject("autority_id", UUID.class));
            authority.setAuthority(Authority.valueOf(rs.getString("authority")));
            user.getAuthorities().add(authority);
        }
        return userMap.get(userId);
    }
}
