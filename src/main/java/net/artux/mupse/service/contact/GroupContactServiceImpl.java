package net.artux.mupse.service.contact;

import lombok.RequiredArgsConstructor;
import net.artux.mupse.entity.contact.ContactEntity;
import net.artux.mupse.entity.contact.ContactGroupEntity;
import net.artux.mupse.model.contact.ContactContainer;
import net.artux.mupse.model.contact.ContactDto;
import net.artux.mupse.model.contact.ContactGroupCreateDto;
import net.artux.mupse.model.contact.ContactGroupDto;
import net.artux.mupse.model.contact.ContactMapper;
import net.artux.mupse.model.contact.CreateContactDto;
import net.artux.mupse.model.contact.ParsingResult;
import net.artux.mupse.model.page.QueryPage;
import net.artux.mupse.model.page.ResponsePage;
import net.artux.mupse.repository.contact.ContactGroupRepository;
import net.artux.mupse.repository.contact.ContactRepository;
import net.artux.mupse.service.user.UserService;
import net.artux.mupse.service.util.PageService;
import net.artux.mupse.service.util.SortService;
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
        ContactGroupEntity groupEntity = groupRepository.findById(id).orElseThrow();
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
        return contactService.exportContacts(contactRepository.findAllByGroupAndOwner(id, userService.getUserEntity().getId()));

    }

    @Override
    public ResponsePage<ContactDto> getContacts(Long id, QueryPage queryPage) {
        Page<ContactEntity> contactEntities = contactRepository.findAllByGroupAndOwner(id, userService.getUserEntity().getId(),
                sortService.getSortInfo(ContactDto.class, queryPage, "name"));
        return pageService.mapDataPageToResponsePage(contactEntities, mapper.dto(contactEntities.getContent()));
    }

    @Override
    public ContactDto createContact(Long id, CreateContactDto dto) {
        ContactGroupEntity groupEntity = groupRepository.findById(id).orElseThrow();
        ContactEntity entity = contactService.createContactEntity(dto);
        if (!groupEntity.getContacts().contains(entity)) {
            groupEntity.getContacts().add(entity);
            groupRepository.save(groupEntity);
        }
        return mapper.dto(entity);
    }

    @Override
    public ResponsePage<ContactGroupDto> getGroups(QueryPage page) {
        Page<ContactGroupEntity> groupEntities = groupRepository.findAllByOwner(userService.getUserEntity(),
                sortService.getSortInfo(ContactGroupDto.class, page, "name"));
        return pageService.mapDataPageToResponsePage(groupEntities, mapper.groupDto(groupEntities.getContent()));
    }

    @Override
    public ContactGroupDto createGroup(ContactGroupCreateDto createDto) {
        if (groupRepository.findByOwnerAndName(userService.getUserEntity(), createDto.getName()).isPresent())
            throw new RuntimeException("Группа с таким именем уже существует.");
        ContactGroupEntity entity = new ContactGroupEntity();
        entity.setName(createDto.getName());
        entity.setDescription(createDto.getDescription());
        return mapper.groupDto(groupRepository.save(entity));
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
    public boolean putContacts(Long id, List<Long> ids) {
        ContactGroupEntity groupEntity = groupRepository.findById(id).orElseThrow();
        List<ContactEntity> entities = contactRepository.findAllById(ids);
        //TODO db logic
        for (ContactEntity c : entities) {
            if (!groupEntity.getContacts().contains(c))
                groupEntity.getContacts().add(c);
        }
        groupRepository.save(groupEntity);
        return true;
    }

    @Override
    public boolean deleteGroup(Long id, boolean deleteContacts) {
        ContactGroupEntity groupEntity = groupRepository.findById(id).orElseThrow();
        if (deleteContacts) {
            contactRepository.deleteAll(groupEntity.getContacts());
        }
        groupRepository.delete(groupEntity);
        return true;
    }

}
