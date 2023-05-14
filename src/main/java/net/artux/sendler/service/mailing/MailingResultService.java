package net.artux.sendler.service.mailing;

import net.artux.sendler.model.mailing.MailingEventDto;
import net.artux.sendler.model.mailing.MailingResultDto;
import net.artux.sendler.model.page.QueryPage;
import net.artux.sendler.model.page.ResponsePage;

public interface MailingResultService {

    MailingResultDto getResult(Long id);
    ResponsePage<MailingEventDto> getEvents(Long id, QueryPage page, String search);

}
