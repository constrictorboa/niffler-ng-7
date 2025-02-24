package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.UserdataUserDao;
import guru.qa.niffler.data.entity.userdata.FriendshipEntity;
import guru.qa.niffler.data.entity.userdata.UserdataUserEntity;
import guru.qa.niffler.model.CurrencyValues;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.tpl.Connections.holder;

public class UserdataUserDAOJdbc implements UserdataUserDao {
    private static final Config CFG = Config.getInstance();

    @Override
    public UserdataUserEntity create(UserdataUserEntity user) {
        try (PreparedStatement ps = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
                "INSERT INTO \"user\" (username, currency, firstname, surname, photo, photo_small, full_name) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
        )) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getCurrency().name());
            ps.setString(3, user.getFirstname());
            ps.setString(4, user.getSurname());
            ps.setBytes(5, user.getPhoto());
            ps.setBytes(6, user.getPhotoSmall());
            ps.setString(7, user.getFullname());

            ps.executeUpdate();

            final UUID generatedKey;
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    generatedKey = rs.getObject("id", UUID.class);
                } else {
                    throw new SQLException("Can`t find id in ResultSet");
                }
            }
            user.setId(generatedKey);
            return user;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserdataUserEntity update(UserdataUserEntity user) {
        try (PreparedStatement usersPs = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
                "UPDATE \"user\" SET currency=?, firstname=?, surname=?, photo=?, photo_small=? WHERE id = ?");

             PreparedStatement friendsPs = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
                     "INSERT INTO friendship (requester_id, addressee_id, status) VALUES (?, ?, ?)" +
                             " ON CONFLICT (requester_id, addressee_id) DO UPDATE SET status = ?")
        ) {
            usersPs.setString(1, user.getCurrency().name());
            usersPs.setString(2, user.getFirstname());
            usersPs.setString(3, user.getSurname());
            usersPs.setBytes(4, user.getPhoto());
            usersPs.setBytes(5, user.getPhotoSmall());
            usersPs.setObject(6, user.getId());
            usersPs.executeUpdate();

            for (FriendshipEntity fe : user.getFriendshipRequests()) {
                friendsPs.setObject(1, user.getId());
                friendsPs.setObject(2, fe.getAddressee().getId());
                friendsPs.setString(3, fe.getStatus().name());
                friendsPs.setString(4, fe.getStatus().name());
                friendsPs.addBatch();
                friendsPs.clearParameters();
            }
            friendsPs.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return user;
    }

    @Override
    public Optional<UserdataUserEntity> findById(UUID id) {

        try (PreparedStatement ps = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
                "SELECT * from \"user\" WHERE id = ?"
        )) {
            ps.setObject(1, id);

            ps.execute();

            try (ResultSet rs = ps.getResultSet()) {
                if (rs.next()) {
                    return Optional.of(extractUserEntityFromResultSet(rs));
                } else {
                    return Optional.empty();
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

    @Override
    public Optional<UserdataUserEntity> findByUsername(String username) {

        try (PreparedStatement ps = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
                "SELECT * from \"user\" WHERE username = ?"
        )) {
            ps.setObject(1, username);

            ps.execute();

            try (ResultSet rs = ps.getResultSet()) {
                if (rs.next()) {
                    return Optional.of(extractUserEntityFromResultSet(rs));
                } else {
                    return Optional.empty();
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

    @Override
    public void deleteUser(UserdataUserEntity user) {

        try (PreparedStatement ps = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
                "DELETE FROM \"user\" WHERE id = ?"
        )) {
            ps.setObject(1, user.getId());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<UserdataUserEntity> findAll() {
        try (PreparedStatement ps = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
                "SELECT * FROM \"user\""
        )) {
            List<UserdataUserEntity> userdataUserEntityList = new ArrayList<>();
            ps.execute();
            try (ResultSet rs = ps.getResultSet()) {
                if (rs.next()) {
                    userdataUserEntityList.add(extractUserEntityFromResultSet(rs));
                }
            }
            return userdataUserEntityList;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private UserdataUserEntity extractUserEntityFromResultSet(ResultSet resultSet) throws SQLException {
        UserdataUserEntity userEntity = new UserdataUserEntity();
        userEntity.setId(resultSet.getObject("id", UUID.class));
        userEntity.setUsername(resultSet.getString("username"));
        userEntity.setCurrency(CurrencyValues.valueOf(resultSet.getString("currency")));
        userEntity.setFirstname(resultSet.getString("firstname"));
        userEntity.setSurname(resultSet.getString("surname"));
        userEntity.setFullname(resultSet.getString("full_name"));
        userEntity.setPhoto(resultSet.getBytes("photo"));
        userEntity.setPhotoSmall(resultSet.getBytes("photo_small"));
        return userEntity;
    }
}
