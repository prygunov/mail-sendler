package net.artux.mupse.service.mailing.queue;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import net.artux.mupse.entity.mailing.MailingEntity;
import net.artux.mupse.entity.mailing.MailingStatus;
import net.artux.mupse.repository.mailing.MailingRepository;
import net.artux.mupse.service.mailing.MailingService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MailingQueueService {

    private final MailingService mailingService;
    private final MailingRepository mailingRepository;

    @Scheduled(fixedRate = 60 * 1000)
    private void checkMailingsQueue() {
        List<MailingEntity> mailings = mailingRepository.findAllByStatusAndTimeBefore(MailingStatus.QUEUE, LocalDateTime.now());
        for (MailingEntity mailing : mailings) {
            mailingService.startMailing(mailing.getId());
        }
    }

}
