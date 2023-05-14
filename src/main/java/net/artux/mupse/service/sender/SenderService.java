package net.artux.mupse.service.sender;

import org.springframework.scheduling.annotation.Async;
import net.artux.mupse.entity.statistic.MailingResultEntity;

import java.util.concurrent.Future;

public interface SenderService {

    @Async
    Future<MailingResultEntity> startMailing(Long id);
    boolean testMail(Long id);
}
