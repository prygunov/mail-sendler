package net.artux.mupse.service.contact;

import lombok.RequiredArgsConstructor;
import net.artux.mupse.entity.contact.ContactEntity;
import net.artux.mupse.entity.contact.ContactGroupEntity;
import net.artux.mupse.model.contact.*;
import net.artux.mupse.model.page.QueryPage;
import net.artux.mupse.model.page.ResponsePage;
import net.artux.mupse.repository.contact.ContactGroupRepository;
import net.artux.mupse.repository.contact.ContactRepository;
import net.artux.mupse.service.user.UserService;
import net.artux.mupse.service.util.PageService;
import net.artux.mupse.service.util.SortService;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupContactServiceImpl implements GroupContactService {

    private final ContactGroupRepository groupRepository;
    private final ContactRepository contactRepository;
    private final ContactMapper mapper;

    private final UserService userService;
    private final TempContactService tempContactService;
    private final ContactServiceImpl contactService;
    private final SortService sortService;
    private final PageService pageService;

    @Override
    public ParsingResult saveContactsFromFile(Long id, MultipartFile file) throws IOException {
        ContactGroupEntity groupEntity = groupRepository.findByOwnerAndId(userService.getUserEntity(), id).orElseThrow();
        ContactContainer container = tempContactService.detect(file);

        List<ContactEntity> saved = contactRepository.saveAll(container.getOriginalContacts());

        List<ContactEntity> groupContacts = groupEntity.getContacts();
        groupContacts.addAll(saved.stream()
                .filter(t -> groupContacts.stream().noneMatch(t::equals))
                .toList());

        groupContacts.addAll(container.getCollisionContacts().stream()
                .filter(t -> groupContacts.stream().noneMatch(t::equals))
                .toList());
        groupEntity.setContacts(groupContacts);
        groupRepository.save(groupEntity);
        int regexRejected = container.getRegexRejected();
        int collisionRejected = container.getCollisionContacts().size();

        return new ParsingResult(file.getOriginalFilename(), container.getAll(), saved.size() + container.getCollisionContacts().size(),
                collisionRejected + regexRejected, regexRejected, collisionRejected);
    }

    @Override
    public ByteArrayInputStream exportContacts(Long id) throws IOException {
        ContactGroupEntity groupEntity = groupRepository.findByOwnerAndId(userService.getUserEntity(), id).orElseThrow();
        return contactService.exportContacts(contactRepository.findAllByGroup(groupEntity));

    }

    @Override
    public ResponsePage<ContactDto> getContacts(Long id, QueryPage queryPage, String search) {
        ContactGroupEntity groupEntity = groupRepository.findByOwnerAndId(userService.getUserEntity(), id).orElseThrow();

        Page<ContactEntity> contactEntities;
        if (search == null)
            contactEntities = contactRepository.findAllByGroup(groupEntity,
                    sortService.getSortInfo(ContactDto.class, queryPage, "name"));
        else
            contactEntities = contactRepository.findAllByGroup(groupEntity, search,
                    sortService.getSortInfo(ContactDto.class, queryPage, "name"));

        return pageService.mapDataPageToResponsePage(contactEntities, mapper.dto(contactEntities.getContent()));
    }

    @Override
    public ResponsePage<ContactGroupDto> getGroups(QueryPage page, String search) {
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
        Example<ContactGroupEntity> example = Example.of(new ContactGroupEntity(userService.getUserEntity(), search), matcher);

        Page<ContactGroupEntity> groupEntities = groupRepository.findAll(example,
                sortService.getSortInfo(ContactGroupDto.class, page, "name"));
        return pageService.mapDataPageToResponsePage(groupEntities, mapper.groupDto(groupEntities.getContent()));
    }

    @Override
    public ContactDto createContact(Long id, CreateContactDto dto) {
        ContactGroupEntity groupEntity = groupRepository.findByOwnerAndId(userService.getUserEntity(), id).orElseThrow();
        ContactEntity entity = contactService.createContactWithOwner(userService.getUserEntity(), dto);
        if (!groupEntity.getContacts().contains(entity)) {
            groupEntity.getContacts().add(entity);
            groupRepository.save(groupEntity);
        }
        return mapper.dto(entity);
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
        ContactGroupEntity group = groupRepository.findByOwnerAndId(userService.getUserEntity(), id).orElseThrow();
        group.setName(createDto.getName());
        group.setDescription(createDto.getDescription());
        group.setOwner(userService.getUserEntity());
        group.setHexColor(createDto.getHexColor());
        return mapper.groupDto(groupRepository.save(group));
    }


    @Override
    public ContactGroupDto getGroup(Long id) {
        return mapper.groupDto(groupRepository.findByOwnerAndId(userService.getUserEntity(), id).orElseThrow());
    }

    @Override
    public List<ContactDto> createContacts(Long id, List<CreateContactDto> dtos) {
        List<ContactDto> result = new LinkedList<>();
        for (CreateContactDto contactDto : dtos) {
            try {
                result.add(createContact(id, contactDto));
            } catch (RuntimeException ignored) {
            }
        }
        return result;
    }

    @Override
    public int putContacts(Long id, List<Long> ids) {
        ContactGroupEntity groupEntity = groupRepository.findByOwnerAndId(userService.getUserEntity(), id).orElseThrow();
        List<ContactEntity> entities = contactRepository.findAllById(ids);
        //TODO db logic
        int addedEntities = 0;
        for (ContactEntity c : entities) {
            if (!groupEntity.getContacts().contains(c)) {
                groupEntity.getContacts().add(c);
                addedEntities++;
            }
        }
        groupRepository.save(groupEntity);
        return addedEntities;
    }

    @Override
    public boolean deleteContactsFromGroup(Long id, List<Long> ids) {
        ContactGroupEntity groupEntity = groupRepository.findByOwnerAndId(userService.getUserEntity(), id).orElseThrow();
        boolean result = groupEntity.getContacts().removeIf(contactEntity -> ids.contains(contactEntity.getId()));
        groupRepository.save(groupEntity);
        return result;
    }

    @Override
    public boolean deleteGroup(Long id, boolean deleteContacts) {
        ContactGroupEntity groupEntity = groupRepository.findByOwnerAndId(userService.getUserEntity(), id).orElseThrow();
        if (deleteContacts) {
            contactRepository.deleteAll(groupEntity.getContacts());
        }
        groupRepository.delete(groupEntity);
        return true;
    }

}
