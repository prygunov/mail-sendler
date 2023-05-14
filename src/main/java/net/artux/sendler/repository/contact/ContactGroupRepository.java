package net.artux.sendler.repository.contact;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import net.artux.sendler.entity.contact.ContactGroupEntity;
import net.artux.sendler.entity.user.UserEntity;

import java.util.Optional;
import java.util.Set;

@Component
public interface ContactGroupRepository extends JpaRepository<ContactGroupEntity, Long> {

    Set<ContactGroupEntity> findAllByIdInAndOwner(Iterable<Long> id, UserEntity entity);

    Optional<ContactGroupEntity> findByOwnerAndName(UserEntity entity, String name);

    Optional<ContactGroupEntity> findByOwnerAndId(UserEntity entity, Long id);

}
