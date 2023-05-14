package net.artux.sendler.repository.reason;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import net.artux.sendler.entity.reason.ReasonEntity;
import net.artux.sendler.entity.user.UserEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public interface ReasonRepository extends JpaRepository<ReasonEntity, Long> {

    @Query("select r from ReasonEntity r where r.contact.owner = ?1")
    List<ReasonEntity> findAllByOwner(UserEntity owner);

    @Query("select r from ReasonEntity r where r.contact.owner = ?1")
    Page<ReasonEntity> findAllByOwner(UserEntity owner, Pageable pageable);

    @Query("select r from ReasonEntity r where r.contact.token = ?1")
    Optional<ReasonEntity> findByContactToken(UUID token);

    @Query("delete from ReasonEntity r where r.contact.owner = ?1")
    void deleteAllByOwner(UserEntity owner);
}
