package net.artux.mupse.repository.user;

import net.artux.mupse.entity.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByLogin(String login);

    Optional<UserEntity> findByToken(UUID uuid);

}
