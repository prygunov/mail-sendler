package net.artux.sendler.service.mailing;

import net.artux.sendler.entity.mailing.MailingStatus;
import net.artux.sendler.model.mailing.MailingCreateDto;
import net.artux.sendler.model.mailing.MailingDto;
import net.artux.sendler.model.page.QueryPage;
import net.artux.sendler.model.page.ResponsePage;

import java.time.LocalDateTime;

public interface MailingService {

    ResponsePage<MailingDto> getMailings(QueryPage page, MailingStatus status);

    MailingDto createMailing(MailingCreateDto createDto);

    MailingDto editMailing(Long id, MailingCreateDto createDto);

    boolean deleteMailingDraft(Long id);

    MailingDto getMailing(Long id);

    MailingDto prepareMailing(Long id);

    MailingDto addMailingToQueue(Long id, LocalDateTime time);

    MailingDto removeMailingFromQueue(Long id);

    void startMailing(Long id);
}
