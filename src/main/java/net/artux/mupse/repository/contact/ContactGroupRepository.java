package net.artux.mupse.repository.contact;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import net.artux.mupse.entity.contact.ContactGroupEntity;
import net.artux.mupse.entity.user.UserEntity;

import java.util.Optional;
import java.util.Set;

@Component
public interface ContactGroupRepository extends JpaRepository<ContactGroupEntity, Long> {

    Set<ContactGroupEntity> findAllByIdInAndOwner(Iterable<Long> id, UserEntity entity);

    Optional<ContactGroupEntity> findByOwnerAndName(UserEntity entity, String name);

    Optional<ContactGroupEntity> findByOwnerAndId(UserEntity entity, Long id);

}
