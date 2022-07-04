package net.artux.mupse.service.util;

import lombok.RequiredArgsConstructor;
import net.artux.mupse.entity.user.UserEntity;
import net.artux.mupse.model.user.SecurityUser;
import net.artux.mupse.repository.user.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService {

    private final UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity entity = repository.findByLogin(username).orElseThrow();
        List<SimpleGrantedAuthority> authorities = Collections
                .singletonList(new SimpleGrantedAuthority(entity.getRole().name()));
        return new SecurityUser(entity.getLogin(), entity.getPassword(), authorities, entity);
    }
}
