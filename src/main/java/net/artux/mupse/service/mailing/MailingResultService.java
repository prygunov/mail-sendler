package net.artux.mupse.service.mailing;

import net.artux.mupse.model.mailing.MailingEventDto;
import net.artux.mupse.model.mailing.MailingResultDto;
import net.artux.mupse.model.page.QueryPage;
import net.artux.mupse.model.page.ResponsePage;

public interface MailingResultService {

    MailingResultDto getResult(Long id);
    ResponsePage<MailingEventDto> getEvents(Long id, QueryPage page, String search);

}
