package net.artux.mupse.repository.user;

import net.artux.mupse.entity.user.UserEntity;
import net.artux.mupse.model.user.UserDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByLogin(String login);

}
