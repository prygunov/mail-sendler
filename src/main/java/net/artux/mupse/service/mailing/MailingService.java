package net.artux.mupse.service.mailing;

import net.artux.mupse.entity.mailing.MailingStatus;
import net.artux.mupse.model.mailing.MailingCreateDto;
import net.artux.mupse.model.mailing.MailingDto;
import net.artux.mupse.model.page.QueryPage;
import net.artux.mupse.model.page.ResponsePage;

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
