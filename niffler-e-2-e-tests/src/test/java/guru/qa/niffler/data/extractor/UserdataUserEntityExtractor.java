package guru.qa.niffler.data.extractor;

import guru.qa.niffler.data.entity.userdata.FriendshipEntity;
import guru.qa.niffler.data.entity.userdata.FriendshipStatus;
import guru.qa.niffler.data.entity.userdata.UserdataUserEntity;
import guru.qa.niffler.model.CurrencyValues;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class UserdataUserEntityExtractor implements ResultSetExtractor<UserdataUserEntity> {
    public static final UserdataUserEntityExtractor instance = new UserdataUserEntityExtractor();

    private UserdataUserEntityExtractor() {
    }

    @Override
    public UserdataUserEntity extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<UUID, UserdataUserEntity> userMap = new ConcurrentHashMap<>();
        UUID userId = null;
        while (rs.next()) {
            userId = rs.getObject("id", UUID.class);
            UserdataUserEntity userdataUserEntity = new UserdataUserEntity();
            if (!userMap.containsKey(userId)) {
                userdataUserEntity.setId(userId);
                try {
                    userdataUserEntity.setUsername(rs.getString("username"));
                    userdataUserEntity.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
                    userdataUserEntity.setFirstname(rs.getString("firstname"));
                    userdataUserEntity.setSurname(rs.getString("surname"));
                    userdataUserEntity.setFullname(rs.getString("full_name"));
                    userdataUserEntity.setPhoto(rs.getBytes("photo"));
                    userdataUserEntity.setPhotoSmall(rs.getBytes("photo_small"));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } else {
                userdataUserEntity = userMap.get(userId);
            }
            if (rs.getObject("created_date") != null) {
                FriendshipEntity friendship = new FriendshipEntity();
                if (rs.getObject("requester_id", UUID.class).equals(userId)) {
                    friendship.setRequester(userdataUserEntity);
                    UserdataUserEntity addressee = new UserdataUserEntity();
                    addressee.setId(rs.getObject("addressee_id", UUID.class));
                    friendship.setAddressee(addressee);
                    friendship.setCreatedDate(rs.getDate("created_date"));
                    friendship.setStatus(FriendshipStatus.valueOf(rs.getString("status")));
                    userdataUserEntity.getFriendshipRequests().add(friendship);
                } else if (rs.getObject("addressee_id", UUID.class).equals(userId)) {
                    friendship.setAddressee(userdataUserEntity);
                    UserdataUserEntity requester = new UserdataUserEntity();
                    requester.setId(rs.getObject("requester_id", UUID.class));
                    friendship.setRequester(requester);
                    friendship.setCreatedDate(rs.getDate("created_date"));
                    friendship.setStatus(FriendshipStatus.valueOf(rs.getString("status")));
                    userdataUserEntity.getFriendshipAddressees().add(friendship);
                }
            }
            userMap.put(userId, userdataUserEntity);

        }
        return userMap.get(userId);
    }
}
