package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.Databases;
import guru.qa.niffler.data.dao.impl.AuthAuthorityDaoJdbc;
import guru.qa.niffler.data.dao.impl.AuthUserDaoJdbc;
import guru.qa.niffler.data.dao.impl.UserdataUserDAOJdbc;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.userdata.UserdataUserEntity;
import guru.qa.niffler.model.UserJson;

import static guru.qa.niffler.data.Databases.xaTransaction;

public class UsersDbClient {
    private static final Config CFG = Config.getInstance();

    public UserJson createUser(UserJson userJson) {
        return UserJson.fromEntity( xaTransaction(1, new Databases.XaFunction<>(connection -> {
                    AuthUserEntity userEntity = new AuthUserEntity();
                    userEntity.setUsername(userJson.username());
                    userEntity.setPassword("12345");
                    userEntity.setEnabled(true);
                    userEntity.setAccountNonExpired(true);
                    userEntity.setAccountNonLocked(true);
                    userEntity.setCredentialsNonExpired(true);
                    userEntity = new AuthUserDaoJdbc(connection).create(userEntity);
                    AuthAuthorityDaoJdbc authAuthorityDaoJdbc = new AuthAuthorityDaoJdbc(connection);
                    userEntity.addAuthorities(authAuthorityDaoJdbc.create(new AuthorityEntity(Authority.write, userEntity)));
                    userEntity.addAuthorities(authAuthorityDaoJdbc.create(new AuthorityEntity(Authority.read, userEntity)));
                    return null;
                }, CFG.authJdbcUrl()),
                new Databases.XaFunction<>(connection -> new UserdataUserDAOJdbc(connection)
                        .create(UserdataUserEntity.fromJson(userJson)), CFG.userdataJdbcUrl())
        ));
    }
}
