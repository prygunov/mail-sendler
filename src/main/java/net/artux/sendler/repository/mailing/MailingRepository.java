package net.artux.sendler.repository.mailing;

import net.artux.sendler.entity.mailing.MailingEntity;
import net.artux.sendler.entity.mailing.MailingStatus;
import net.artux.sendler.entity.user.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public interface MailingRepository extends JpaRepository<MailingEntity, Long> {

    List<MailingEntity> findAllByStatusAndTimeBefore(MailingStatus status, LocalDateTime time);

    Optional<MailingEntity> findByOwnerAndId(UserEntity user, Long id);

    Page<MailingEntity> findAllByOwnerAndStatus(UserEntity user, MailingStatus status, Pageable pageable);

    Optional<MailingEntity> findByToken(UUID mailingUuid);
}
