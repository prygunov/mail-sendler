package net.artux.mupse.service.statistic;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import net.artux.mupse.entity.statistic.ContactEventEntity;
import net.artux.mupse.entity.statistic.EventType;
import net.artux.mupse.model.contact.ContactEventDto;
import net.artux.mupse.model.contact.ContactMapper;
import net.artux.mupse.model.page.QueryPage;
import net.artux.mupse.model.page.ResponsePage;
import net.artux.mupse.repository.contact.ContactRepository;
import net.artux.mupse.repository.mailing.MailingRepository;
import net.artux.mupse.repository.statistic.ContactEventRepository;
import net.artux.mupse.service.user.UserService;
import net.artux.mupse.service.util.PageService;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StatisticServiceImpl implements StatisticService {

    private final ContactEventRepository contactEventRepository;
    private final ContactRepository contactRepository;
    private final MailingRepository mailingRepository;
    private final UserService userService;
    private final PageService pageService;
    private final ContactMapper contactMapper;

    @Override
    public void opened(UUID mailingUuid, UUID contactUuid) {
        save(mailingUuid, contactUuid, EventType.OPENED);
    }

    @Override
    public void redirect(UUID mailingUuid, UUID contactUuid) {
        save(mailingUuid, contactUuid, EventType.REDIRECT);
    }

    @Override
    public ResponsePage<ContactEventDto> getEvents(Long id, QueryPage queryPage, EventType type) {
        Page<ContactEventEntity> page = contactEventRepository
                .findAllByContactOwner(userService.getUserEntity(), id, type, pageService.getPageable(queryPage));
        return pageService.mapDataPageToResponsePage(page, contactMapper::dto);
    }

    private void save(UUID mailingUuid, UUID contactUuid, EventType type) {
        ContactEventEntity contactEventEntity = new ContactEventEntity();
        contactEventEntity.setContact(contactRepository.findByToken(contactUuid).orElseThrow());
        contactEventEntity.setMailing(mailingRepository.findByToken(mailingUuid).orElseThrow());
        contactEventEntity.setType(type);
        contactEventEntity.setTime(LocalDateTime.now());

        contactEventRepository.save(contactEventEntity);
    }
}
