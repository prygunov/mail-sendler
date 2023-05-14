package net.artux.sendler.service.sender;

import org.springframework.scheduling.annotation.Async;
import net.artux.sendler.entity.statistic.MailingResultEntity;

import java.util.concurrent.Future;

public interface SenderService {

    @Async
    Future<MailingResultEntity> startMailing(Long id);
    boolean testMail(Long id);
}
