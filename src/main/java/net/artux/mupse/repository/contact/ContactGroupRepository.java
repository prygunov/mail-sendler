package net.artux.mupse.repository.contact;

import net.artux.mupse.entity.contact.ContactGroupEntity;
import net.artux.mupse.entity.user.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface ContactGroupRepository extends JpaRepository<ContactGroupEntity, Long> {

    Page<ContactGroupEntity> findAllByOwner(UserEntity entity, Pageable pageable);
    Optional<ContactGroupEntity> findByOwnerAndName(UserEntity entity, String name);

}
