package net.artux.mupse.repository.settings;

import net.artux.mupse.entity.settings.UserMailEntity;
import net.artux.mupse.entity.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public interface UserMailRepository extends JpaRepository<UserMailEntity, Long> {

    Optional<UserMailEntity> findByOwnerAndId(UserEntity owner, Long id);

    List<UserMailEntity> findAllByOwner(UserEntity owner);
}
