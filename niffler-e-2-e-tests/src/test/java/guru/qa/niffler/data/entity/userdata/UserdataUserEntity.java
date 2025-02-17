package guru.qa.niffler.data.entity.userdata;

import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.UserJson;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Getter
@Setter
public class UserdataUserEntity implements Serializable {
    private UUID id;
    private String username;
    private CurrencyValues currency;
    private String firstname;
    private String surname;
    private String fullname;
    private byte[] photo;
    private byte[] photoSmall;

    public static UserdataUserEntity fromJson(UserJson json) {
        UserdataUserEntity userEntity = new UserdataUserEntity();
        userEntity.setId(json.id());
        userEntity.setUsername(json.username());
        userEntity.setCurrency(json.currency());
        userEntity.setFirstname(json.firstname());
        userEntity.setSurname(json.surname());
        userEntity.setFullname(json.fullname());
        userEntity.setPhoto(json.photo() != null ? json.photo().getBytes(StandardCharsets.UTF_8) : null);
        userEntity.setPhotoSmall(json.photoSmall() != null ? json.photoSmall().getBytes(StandardCharsets.UTF_8) : null);
        return userEntity;
    }
}