package net.artux.sendler.service.statistic;

import net.artux.sendler.entity.statistic.EventType;
import net.artux.sendler.model.contact.ContactEventDto;
import net.artux.sendler.model.page.QueryPage;
import net.artux.sendler.model.page.ResponsePage;

import java.util.UUID;

public interface StatisticService {

    void opened(UUID mailingUuid, UUID contactUuid);

    void redirect(UUID mailingUuid, UUID contactUuid);

    ResponsePage<ContactEventDto> getEvents(Long id, QueryPage page, EventType type);

}
