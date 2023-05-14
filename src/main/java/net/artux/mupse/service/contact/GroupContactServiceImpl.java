package net.artux.mupse.service.contact;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import net.artux.mupse.entity.contact.ContactEntity;
import net.artux.mupse.entity.contact.ContactGroupEntity;
import net.artux.mupse.model.contact.ContactDto;
import net.artux.mupse.model.contact.ContactGroupCreateDto;
import net.artux.mupse.model.contact.ContactGroupDto;
import net.artux.mupse.model.contact.ContactMapper;
import net.artux.mupse.model.contact.creation.ContactCreationResult;
import net.artux.mupse.model.contact.creation.ContactCreationResultDto;
import net.artux.mupse.model.contact.creation.ContactMassiveCreateDto;
import net.artux.mupse.model.contact.creation.CreationMapper;
import net.artux.mupse.model.exception.exceptions.NotFoundException;
import net.artux.mupse.model.page.QueryPage;
import net.artux.mupse.model.page.ResponsePage;
import net.artux.mupse.repository.contact.ContactGroupRepository;
import net.artux.mupse.repository.contact.ContactRepository;
import net.artux.mupse.service.user.UserService;
import net.artux.mupse.service.util.PageService;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class GroupContactServiceImpl implements GroupContactService {

    private final ContactGroupRepository groupRepository;
    private final ContactRepository contactRepository;
    private final ContactMapper mapper;
    private final CreationMapper creationMapper;

    private final UserService userService;
    private final ContactRegistrationService contactRegistrationService;
    private final ContactService contactService;
    private final PageService pageService;

    @Override
    public ContactCreationResultDto saveContactsFromFile(Long id, MultipartFile file) throws IOException {
        ContactGroupEntity groupEntity = getEntity(id);
        ContactCreationResult result = contactRegistrationService.contactsContactsFromFile(file);

        return setupContactsWithGroup(groupEntity, result);
    }

    @Override
    public ByteArrayInputStream exportContacts(Long id) throws IOException {
        ContactGroupEntity groupEntity = getEntity(id);
        return contactService.exportContacts(contactRepository.findAllByGroup(groupEntity));
    }

    @Override
    public ResponsePage<ContactDto> getContacts(Long id, QueryPage queryPage, String search) {
        ContactGroupEntity groupEntity = getEntity(id);

        Page<ContactEntity> contactEntities;
        if (search == null)
            contactEntities = contactRepository.findAllByGroup(groupEntity, pageService.getPageable(queryPage));
        else
            contactEntities = contactRepository.findAllByGroup(groupEntity, search, pageService.getPageable(queryPage));

        return pageService.mapDataPageToResponsePage(contactEntities, mapper::dto);
    }

    @Override
    public ResponsePage<ContactGroupDto> getGroups(QueryPage page, String search) {
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
        Example<ContactGroupEntity> example = Example
                .of(new ContactGroupEntity(userService.getUserEntity(), search), matcher);

        Page<ContactGroupEntity> groupEntities = groupRepository.findAll(example, pageService.getPageable(page));
        return pageService.mapDataPageToResponsePage(groupEntities, mapper::groupDto);
    }

    @Override
    public List<ContactGroupDto> getGroups(String search) {
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
        Example<ContactGroupEntity> example = Example
                .of(new ContactGroupEntity(userService.getUserEntity(), search), matcher);

        return mapper.groupDto(groupRepository.findAll(example));
    }

    @Override
    public ContactGroupDto createGroup(ContactGroupCreateDto createDto) {
        if (groupRepository.findByOwnerAndName(userService.getUserEntity(), createDto.getName()).isPresent())
            throw new RuntimeException("Группа с таким именем уже существует.");
        ContactGroupEntity group = new ContactGroupEntity();
        group.setName(createDto.getName());
        group.setDescription(createDto.getDescription());
        group.setOwner(userService.getUserEntity());
        group.setHexColor(createDto.getHexColor());
        return mapper.groupDto(groupRepository.save(group));
    }

    @Override
    public ContactGroupDto editGroup(Long id, ContactGroupCreateDto createDto) {
        ContactGroupEntity group = getEntity(id);
        group.setName(createDto.getName());
        group.setDescription(createDto.getDescription());
        group.setHexColor(createDto.getHexColor());
        return mapper.groupDto(groupRepository.save(group));
    }


    @Override
    public ContactGroupDto getGroup(Long id) {
        return mapper.groupDto(getEntity(id));
    }

    @Override
    public ContactCreationResultDto createContacts(Long id, List<ContactMassiveCreateDto> dto) {
        ContactGroupEntity group = getEntity(id);
        ContactCreationResult result = contactRegistrationService.createContacts(dto);

        return setupContactsWithGroup(group, result);
    }

    private ContactCreationResultDto setupContactsWithGroup(ContactGroupEntity group, ContactCreationResult result) {
        Set<ContactEntity> groupContacts = group.getContacts();
        groupContacts.addAll(result.getOriginalContacts());
        groupContacts.addAll(result.getCollisionContacts());
        group.setContacts(groupContacts);
        groupRepository.save(group);

        return creationMapper.dto(result);
    }

    @Override
    public boolean putContacts(Long id, List<Long> ids) {
        ContactGroupEntity groupEntity = getEntity(id);
        List<ContactEntity> entities = contactRepository.findAllById(ids);
        groupEntity.getContacts().addAll(entities);
        groupRepository.save(groupEntity);
        return true;
    }

    @Override
    public boolean deleteContactsFromGroup(Long id, List<Long> ids) {
        ContactGroupEntity groupEntity = getEntity(id);
        boolean result = groupEntity.getContacts().removeIf(contactEntity -> ids.contains(contactEntity.getId()));
        groupRepository.save(groupEntity);
        return result;
    }

    @Override
    @Transactional
    public boolean deleteGroup(Long id, boolean deleteContacts) {
        ContactGroupEntity groupEntity = getEntity(id);
        if (deleteContacts) {
            contactRepository.deleteAll(groupEntity.getContacts());
        }
        groupRepository.delete(groupEntity);
        return true;
    }

    private ContactGroupEntity getEntity(Long id) {
        return groupRepository.findByOwnerAndId(userService.getUserEntity(), id)
                .orElseThrow(() -> new NotFoundException("Группа не найдена"));
    }
}
