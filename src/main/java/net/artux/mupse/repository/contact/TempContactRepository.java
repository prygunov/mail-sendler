package net.artux.mupse.repository.contact;

import net.artux.mupse.entity.contact.TempContactEntity;
import net.artux.mupse.entity.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public interface TempContactRepository extends JpaRepository<TempContactEntity, Long> {

    @Transactional
    @Modifying
    @Query(value = "delete from temp_contact where id" +
            " not in (select min(id) from temp_contact c group by email) and owner_id = ?1", nativeQuery = true)
    int deleteDuplicateEmail(Long ownerId);

    @Query(value = "select * from temp_contact tc where tc.owner_id = ?1 and email not in " +
            "(select email from contact c where c.owner_id = ?1)", nativeQuery = true)
    List<TempContactEntity> getOriginalContacts(Long ownerId);

    @Transactional
    @Modifying
    @Query(value = "delete from temp_contact tc where tc.owner_id = ?1 and email !~* '^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$' ", nativeQuery = true)
    int deleteNotValidContacts(Long ownerId);

    @Transactional
    @Modifying
    int deleteAllByOwner(UserEntity user);

}
