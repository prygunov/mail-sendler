package net.artux.sendler.service.mailing.queue;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import net.artux.sendler.entity.mailing.MailingEntity;
import net.artux.sendler.entity.mailing.MailingStatus;
import net.artux.sendler.repository.mailing.MailingRepository;
import net.artux.sendler.service.mailing.MailingService;

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
