package net.artux.sendler.repository.statistic;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import net.artux.sendler.entity.mailing.MailingEntity;
import net.artux.sendler.entity.statistic.MailingEventEntity;

import java.util.List;

@Component
public interface MailingEventRepository extends JpaRepository<MailingEventEntity, Long> {

    List<MailingEventEntity> findByMailing(MailingEntity entity);
    long countAllByMailing(MailingEntity entity);
}
