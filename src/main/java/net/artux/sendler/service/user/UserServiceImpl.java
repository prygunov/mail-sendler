package net.artux.sendler.service.user;

import lombok.RequiredArgsConstructor;
import net.artux.sendler.entity.user.RoleType;
import net.artux.sendler.entity.user.UserEntity;
import net.artux.sendler.model.user.RegisterUserDto;
import net.artux.sendler.model.user.SecurityUser;
import net.artux.sendler.model.user.UserDto;
import net.artux.sendler.model.user.UserMapper;
import net.artux.sendler.repository.user.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final UserMapper mapper;
    private final PasswordEncoder encoder;

    @Override
    public UserDto registerUser(RegisterUserDto registerUser) {
        if (repository.findByLogin(registerUser.getLogin()).isPresent()) {
            throw new RuntimeException("Логин уже занят");
        }
        if (repository.findByEmail(registerUser.getEmail()).isPresent()) {
            throw new RuntimeException("Почта уже занята");
        }
        UserEntity user = mapper.entity(registerUser);
        user.setRole(RoleType.USER);
        user.setPassword(encoder.encode(user.getPassword()));
        user.setToken(UUID.randomUUID());
        return mapper.dto(repository.save(user));
    }

    @Override
    public UserDto findByLogin(String login) {
        return mapper.dto(repository.findByLogin(login)
                .orElseThrow((Supplier<RuntimeException>) () -> new UsernameNotFoundException("Пользователь не найден")));
    }

    @Override
    public UserDto getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return mapper.dto(
                ((SecurityUser) authentication.getPrincipal()).getUserEntity());
    }

    @Override
    public UserEntity getUserEntity() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ((SecurityUser) authentication.getPrincipal()).getUserEntity();
    }
}
