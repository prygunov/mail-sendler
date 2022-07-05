package net.artux.mupse.repository.contact;

import net.artux.mupse.entity.contact.ContactEntity;
import net.artux.mupse.entity.contact.ContactGroupEntity;
import net.artux.mupse.entity.user.UserEntity;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Component
public interface ContactRepository extends JpaRepository<ContactEntity, Long> {

    Optional<ContactEntity> findByOwner(UserEntity owner);

    Optional<ContactEntity> findByOwnerAndId(UserEntity owner, Long id);

    Optional<ContactEntity> findByOwnerAndEmailIgnoreCase(UserEntity owner, String email);

    List<ContactEntity> findAllByOwner(UserEntity owner);

    Page<ContactEntity> findAllByOwner(UserEntity owner, Example<ContactEntity> example, Pageable pageable);

    List<ContactEntity> findAllByEmailInAndOwner(Set<String> emails, UserEntity entity);

    @Query(value = "SELECT c FROM ContactEntity c WHERE :group MEMBER OF c.groups")
    Page<ContactEntity> findAllByGroup(ContactGroupEntity group, Pageable pageable);

    @Query(value = "SELECT c FROM ContactEntity c WHERE :group MEMBER OF c.groups and lower(c.name) like lower(concat('%', :search, '%'))")
    Page<ContactEntity> findAllByGroup(ContactGroupEntity group, String search, Pageable pageable);

    @Query(value = "SELECT c FROM ContactEntity c WHERE :group MEMBER OF c.groups")
    List<ContactEntity> findAllByGroup(ContactGroupEntity group);

    @Query(value = "select * from contact c where c.owner_id = ?1 and email in " +
            "(select email from temp_contact tc where tc.owner_id = ?1)", nativeQuery = true)
    List<ContactEntity> getCollisionContacts(Long ownerId);

    void deleteAllByOwner(UserEntity user);

    Optional<ContactEntity> findByToken(UUID uuid);

}
