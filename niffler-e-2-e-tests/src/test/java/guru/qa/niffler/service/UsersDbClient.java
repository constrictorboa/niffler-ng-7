package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.Databases;
import guru.qa.niffler.data.dao.impl.AuthAuthorityDaoJdbc;
import guru.qa.niffler.data.dao.impl.AuthAuthorityDaoSpringJdbc;
import guru.qa.niffler.data.dao.impl.AuthUserDaoJdbc;
import guru.qa.niffler.data.dao.impl.AuthUserDaoSpringJdbc;
import guru.qa.niffler.data.dao.impl.UdUserDaoSpringJdbc;
import guru.qa.niffler.data.dao.impl.UserdataUserDAOJdbc;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.userdata.UserdataUserEntity;
import guru.qa.niffler.model.UserJson;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;

import static guru.qa.niffler.data.Databases.dataSource;
import static guru.qa.niffler.data.Databases.xaTransaction;

public class UsersDbClient {
    private static final Config CFG = Config.getInstance();
    private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    public UserJson createUserSpringJdbc(UserJson user) {
        AuthUserEntity authUser = new AuthUserEntity();
        authUser.setUsername(user.username());
        authUser.setPassword(pe.encode("12345"));
        authUser.setEnabled(true);
        authUser.setAccountNonExpired(true);
        authUser.setAccountNonLocked(true);
        authUser.setCredentialsNonExpired(true);

        AuthUserEntity createdAuthUser = new AuthUserDaoSpringJdbc(dataSource(CFG.authJdbcUrl()))
                .create(authUser);

        AuthorityEntity[] authorityEntities = Arrays.stream(Authority.values()).map(
                e -> {
                    AuthorityEntity ae = new AuthorityEntity();
                    ae.setId(createdAuthUser.getId());
                    ae.setAuthority(e);
                    return ae;
                }
        ).toArray(AuthorityEntity[]::new);

        new AuthAuthorityDaoSpringJdbc(dataSource(CFG.authJdbcUrl()))
                .create(authorityEntities);

        return UserJson.fromEntity(
                new UdUserDaoSpringJdbc(dataSource(CFG.userdataJdbcUrl()))
                        .create(
                                UserdataUserEntity.fromJson(user)
                        ),
                null
        );
    }


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
