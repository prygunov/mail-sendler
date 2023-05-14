package net.artux.mupse.service.statistic;

import net.artux.mupse.entity.statistic.EventType;
import net.artux.mupse.model.contact.ContactEventDto;
import net.artux.mupse.model.page.QueryPage;
import net.artux.mupse.model.page.ResponsePage;

import java.util.UUID;

public interface StatisticService {

    void opened(UUID mailingUuid, UUID contactUuid);

    void redirect(UUID mailingUuid, UUID contactUuid);

    ResponsePage<ContactEventDto> getEvents(Long id, QueryPage page, EventType type);

}
