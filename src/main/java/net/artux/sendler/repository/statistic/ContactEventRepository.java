package net.artux.sendler.repository.statistic;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import net.artux.sendler.entity.mailing.MailingEntity;
import net.artux.sendler.entity.statistic.ContactEventEntity;
import net.artux.sendler.entity.statistic.EventType;
import net.artux.sendler.entity.user.UserEntity;

@Component
public interface ContactEventRepository extends JpaRepository<ContactEventEntity, Long> {

    long countAllByMailingAndType(MailingEntity entity, EventType type);

    @Query(value = "select e from ContactEventEntity e where e.contact.owner = ?1 and e.mailing.id = ?2 and e.type = ?3")
    Page<ContactEventEntity> findAllByContactOwner(UserEntity owner, Long mailingId, EventType type, Pageable pageable);

}
