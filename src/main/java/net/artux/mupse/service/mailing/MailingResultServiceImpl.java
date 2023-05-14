package net.artux.mupse.service.mailing;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import net.artux.mupse.entity.contact.ContactEntity;
import net.artux.mupse.entity.mailing.MailingEntity;
import net.artux.mupse.entity.statistic.EventType;
import net.artux.mupse.entity.statistic.MailingEventEntity;
import net.artux.mupse.entity.user.UserEntity;
import net.artux.mupse.model.mailing.MailingEventDto;
import net.artux.mupse.model.mailing.MailingMapper;
import net.artux.mupse.model.mailing.MailingResultDto;
import net.artux.mupse.model.page.QueryPage;
import net.artux.mupse.model.page.ResponsePage;
import net.artux.mupse.repository.mailing.MailingRepository;
import net.artux.mupse.repository.mailing.MailingResultRepository;
import net.artux.mupse.repository.statistic.ContactEventRepository;
import net.artux.mupse.repository.statistic.MailingEventRepository;
import net.artux.mupse.service.user.UserService;
import net.artux.mupse.service.util.PageService;

@Service
@RequiredArgsConstructor
public class MailingResultServiceImpl implements MailingResultService {

    private final ContactEventRepository contactEventRepository;
    private final MailingEventRepository mailingEventRepository;
    private final MailingResultRepository resultRepository;
    private final MailingRepository mailingRepository;

    private final UserService userService;
    private final PageService pageService;
    private final MailingMapper mailingMapper;

    @Override
    public MailingResultDto getResult(Long id) {
        UserEntity user = userService.getUserEntity();
        MailingEntity mailing = mailingRepository.findByOwnerAndId(user, id).orElseThrow();
        long openings = contactEventRepository.countAllByMailingAndType(mailing, EventType.OPENED);
        long linkOpenings = contactEventRepository.countAllByMailingAndType(mailing, EventType.REDIRECT);
        long events = mailingEventRepository.countAllByMailing(mailing);

        return mailingMapper.dto(resultRepository.findByMailing(mailing).orElseThrow(),
                openings,
                linkOpenings,
                events);
    }

    @Override
    public ResponsePage<MailingEventDto> getEvents(Long id, QueryPage page, String search) {
        UserEntity user = userService.getUserEntity();
        MailingEntity mailing = mailingRepository.findByOwnerAndId(user, id).orElseThrow();

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("content", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
        Example<MailingEventEntity> example = Example.of(new MailingEventEntity(mailing, search), matcher);
        Page<MailingEventEntity> contactEntities = mailingEventRepository.findAll(example, pageService.getPageable(page));
        return pageService.mapDataPageToResponsePage(contactEntities, mailingMapper::dto);
    }
}
