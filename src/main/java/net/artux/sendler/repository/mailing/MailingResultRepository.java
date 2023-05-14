package net.artux.sendler.repository.mailing;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import net.artux.sendler.entity.mailing.MailingEntity;
import net.artux.sendler.entity.statistic.MailingResultEntity;

import java.util.Optional;

@Component
public interface MailingResultRepository extends JpaRepository<MailingResultEntity, Long> {

    Optional<MailingResultEntity> findByMailing(MailingEntity entity);
}
