package net.artux.mupse.service.mailing;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import net.artux.mupse.entity.mailing.MailingEntity;
import net.artux.mupse.entity.mailing.MailingStatus;
import net.artux.mupse.entity.user.UserEntity;
import net.artux.mupse.model.exception.exceptions.NotFoundException;
import net.artux.mupse.model.mailing.MailingCreateDto;
import net.artux.mupse.model.mailing.MailingDto;
import net.artux.mupse.model.mailing.MailingMapper;
import net.artux.mupse.model.page.QueryPage;
import net.artux.mupse.model.page.ResponsePage;
import net.artux.mupse.repository.contact.ContactGroupRepository;
import net.artux.mupse.repository.mailing.MailingRepository;
import net.artux.mupse.repository.settings.UserMailRepository;
import net.artux.mupse.service.sender.SenderService;
import net.artux.mupse.service.user.UserService;
import net.artux.mupse.service.util.PageService;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MailingServiceImpl implements MailingService {

    private final UserService userService;
    private final PageService pageService;
    private final SenderService senderService;

    private final MailingRepository mailingRepository;
    private final UserMailRepository userMailRepository;
    private final ContactGroupRepository contactGroupRepository;
    private final MailingMapper mapper;


    @Override
    public ResponsePage<MailingDto> getMailings(QueryPage page, MailingStatus status) {
        Page<MailingEntity> mailingEntityPage = mailingRepository.findAllByOwnerAndStatus(userService.getUserEntity(), status,
                pageService.getPageable(page));
        return pageService.mapDataPageToResponsePage(mailingEntityPage, mapper::dto);
    }

    @Override
    public MailingDto createMailing(MailingCreateDto createDto) {
        MailingEntity entity = map(new MailingEntity(), createDto);
        return mapper.dto(mailingRepository.save(entity));
    }

    private MailingEntity map(MailingEntity entity, MailingCreateDto createDto) {
        UserEntity owner = userService.getUserEntity();
        entity.setMail(userMailRepository.findByOwnerAndId(owner, createDto.getMailId())
                .orElseThrow());
        entity.setOwner(owner);

        entity.setNameFrom(createDto.getNameFrom());
        entity.setSubject(createDto.getSubject());
        entity.setContent(createDto.getContent());

        entity.setGroups(contactGroupRepository.findAllByIdInAndOwner(createDto.getGroupIds(), owner));
        entity.setStatus(MailingStatus.DRAFT);
        entity.setTime(LocalDateTime.now());
        return entity;
    }

    @Override
    public MailingDto editMailing(Long id, MailingCreateDto createDto) {
        MailingEntity entity = getEntity(id);
        if (entity.getStatus() != MailingStatus.DRAFT)
            throw new RuntimeException();
        return mapper.dto(mailingRepository.save(map(entity, createDto)));
    }

    @Override
    public MailingDto getMailing(Long id) {
        MailingEntity entity = getEntity(id);
        return mapper.dto(entity);
    }

    @Override
    public boolean deleteMailingDraft(Long id) {
        MailingEntity entity = getEntity(id);
        if (entity.getStatus() != MailingStatus.DRAFT)
            throw new RuntimeException();
        mailingRepository.delete(entity);
        return true;
    }

    //для вызова извне, осуществляет проверку принадлежности рассылки юзеру
    @Override
    public MailingDto prepareMailing(Long id) {
        MailingEntity mailing = getEntity(id);
        startMailing(mailing.getId());
        return mapper.dto(mailing);
    }

    //не вызывать из контроллеров
    @Override
    public void startMailing(Long id) {
        MailingEntity entity = mailingRepository.findById(id).orElseThrow();
        entity.setStatus(MailingStatus.RUNNING);
        mailingRepository.save(entity);
        senderService.startMailing(id);
    }

    @Override
    public MailingDto addMailingToQueue(Long id, LocalDateTime time) {
        MailingEntity entity = getEntity(id);
        entity.setStatus(MailingStatus.QUEUE);
        entity.setTime(time);
        return mapper.dto(mailingRepository.save(entity));
    }

    @Override
    public MailingDto removeMailingFromQueue(Long id) {
        MailingEntity entity = getEntity(id);
        entity.setStatus(MailingStatus.DRAFT);
        entity.setTime(LocalDateTime.now());
        return mapper.dto(mailingRepository.save(entity));
    }

    private MailingEntity getEntity(Long id) {
        return mailingRepository.findByOwnerAndId(userService.getUserEntity(), id)
                .orElseThrow(() -> new NotFoundException("Рассылка не найдена"));
    }

}
