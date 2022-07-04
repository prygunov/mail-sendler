package net.artux.mupse.service.contact;

import com.opencsv.CSVWriter;
import lombok.RequiredArgsConstructor;
import net.artux.mupse.entity.contact.ContactEntity;
import net.artux.mupse.model.contact.ContactContainer;
import net.artux.mupse.model.contact.ContactDto;
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

import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContactServiceImpl implements ContactService {

    private final ContactRepository repository;
    private final ContactGroupRepository groupRepository;
    private final ContactMapper mapper;

    private final UserService userService;
    private final TempContactService tempContactService;
    private final SortService sortService;
    private final PageService pageService;


    @Override
    public ParsingResult saveContactsFromFile(MultipartFile file) throws IOException {
        ContactContainer container = tempContactService.detect(file);
        int accepted = repository.saveAll(container.getOriginalContacts()).size();
        int regexRejected = container.getRegexRejected();
        int collisionRejected = container.getCollisionContacts().size();

        return new ParsingResult(file.getOriginalFilename(), container.getAll(), accepted,
                collisionRejected + regexRejected, regexRejected, collisionRejected);
    }


    @Override
    public void exportContacts(Writer writer) throws IOException {
        CSVWriter csvWriter = new CSVWriter(writer);
        String[] content = new String[2];
        for (ContactEntity contactEntity : repository.findAllByOwner(userService.getUserEntity())) {
            content[0] = contactEntity.getName();
            content[1] = contactEntity.getEmail();
            csvWriter.writeNext(content);
        }

        csvWriter.close();
    }

    @Override
    public ResponsePage<ContactDto> getContacts(QueryPage queryPage) {
        Page<ContactEntity> contactEntities = repository.findAllByOwner(userService.getUserEntity(), sortService.getSortInfo(ContactDto.class, queryPage, "name"));
        return pageService.mapDataPageToResponsePage(contactEntities, mapper.dto(contactEntities.getContent()));
    }

    ContactEntity createContactEntity(CreateContactDto dto) {
        if (repository.findByOwnerAndEmailIgnoreCase(userService.getUserEntity(), dto.getEmail()).isPresent())
            throw new RuntimeException("Контакт с таким адресом уже существует.");

        ContactEntity entity = new ContactEntity();
        dto.setName(dto.getName());
        dto.setEmail(dto.getEmail());
        return repository.save(entity);
    }

    @Override
    public ContactDto createContact(CreateContactDto dto) {
        return mapper.dto(createContactEntity(dto));
    }

    @Override
    public List<ContactDto> createContacts(List<CreateContactDto> dtos) {
        List<ContactDto> result = new LinkedList<>();
        for (CreateContactDto contactDto : dtos){
            try {
                result.add(mapper.dto(createContactEntity(contactDto)));
            }catch (RuntimeException ignored){}
        }
        return result;
    }


    @Override
    public ContactDto editContact(ContactDto contactDto) {
        ContactEntity entity = repository.findById(contactDto.getId()).orElseThrow();
        entity.setEmail(contactDto.getEmail());
        entity.setName(contactDto.getName());

        return mapper.dto(repository.save(entity));
    }

    @Override
    public boolean deleteContacts(Long[] id) {
        repository.deleteAllById(Arrays.stream(id).toList());
        return true;
    }

}
