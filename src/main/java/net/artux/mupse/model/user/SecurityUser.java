package net.artux.mupse.model.user;

import lombok.Getter;
import net.artux.mupse.entity.user.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
public class SecurityUser extends User {

    private final UserEntity userEntity;

    public SecurityUser(String username, String password, Collection<? extends GrantedAuthority> authorities, UserEntity userEntity) {
        super(username, password, authorities);
        this.userEntity = userEntity;
    }

}
