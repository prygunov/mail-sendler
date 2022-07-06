package net.artux.mupse.service.contact;

import lombok.RequiredArgsConstructor;
import net.artux.mupse.entity.contact.ContactEntity;
import net.artux.mupse.entity.user.UserEntity;
import net.artux.mupse.model.contact.*;
import net.artux.mupse.model.page.QueryPage;
import net.artux.mupse.model.page.ResponsePage;
import net.artux.mupse.repository.contact.ContactGroupRepository;
import net.artux.mupse.repository.contact.ContactRepository;
import net.artux.mupse.repository.contact.TempContactRepository;
import net.artux.mupse.service.user.UserService;
import net.artux.mupse.service.util.PageService;
import net.artux.mupse.service.util.SortService;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ContactServiceImpl implements ContactService {

    private final ContactRepository repository;
    private final ContactGroupRepository groupRepository;
    private final TempContactRepository tempRepository;
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
    public ByteArrayInputStream exportAllContacts() throws IOException {
        return exportContacts(repository.findAllByOwner(userService.getUserEntity()));
    }

    public ByteArrayInputStream exportContacts(List<ContactEntity> contacts) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("contacts");

        CellStyle headerCellStyle = workbook.createCellStyle();
        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellStyle(headerCellStyle);

        cell = row.createCell(1);
        cell.setCellStyle(headerCellStyle);

        for (int i = 0; i < contacts.size(); i++) {
            Row header = sheet.createRow(i);
            ContactEntity contactEntity = contacts.get(i);
            Cell headerCell = header.createCell(0);
            headerCell.setCellStyle(headerCellStyle);
            headerCell.setCellValue(contactEntity.getEmail());

            headerCell = header.createCell(1);
            headerCell.setCellStyle(headerCellStyle);
            headerCell.setCellValue(contactEntity.getName());
        }
        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);


        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();
        return new ByteArrayInputStream(outputStream.toByteArray());
    }


    @Override
    public ResponsePage<ContactDto> getContacts(QueryPage queryPage, String search) {
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
        Example<ContactEntity> example = Example.of(new ContactEntity(userService.getUserEntity(), search), matcher);
        Page<ContactEntity> contactEntities = repository.findAll(example, sortService.getSortInfo(ContactDto.class, queryPage, "name"));
        return pageService.mapDataPageToResponsePage(contactEntities, mapper.dto(contactEntities.getContent()));
    }

    @Override
    public ContactEntity createContactWithOwner(UserEntity user, ContactCreateDto dto) {
        if (repository.findByOwnerAndEmailIgnoreCase(userService.getUserEntity(), dto.getEmail()).isPresent())
            throw new RuntimeException("Контакт с таким адресом уже существует.");

        ContactEntity entity = new ContactEntity();
        entity.setName(dto.getName());
        entity.setEmail(dto.getEmail());
        entity.setOwner(user);
        entity.setToken(UUID.randomUUID());
        return repository.save(entity);
    }

    @Override
    public List<ContactDto> createContacts(List<ContactCreateDto> dtos) {
        UserEntity user = userService.getUserEntity();
        List<ContactDto> result = new LinkedList<>();
        for (ContactCreateDto contactDto : dtos) {
            try {
                result.add(mapper.dto(createContactWithOwner(user, contactDto)));
            } catch (RuntimeException ignored) {
            }
        }
        return result;
    }

    @Override
    @Transactional
    public boolean deleteAllContacts() {
        repository.deleteAllByOwner(userService.getUserEntity());
        tempRepository.deleteAllByOwner(userService.getUserEntity());
        return true;
    }

    @Override
    public ContactDto getContact(Long id) {
        return mapper.dto(repository.findByOwnerAndId(userService.getUserEntity(), id).orElseThrow());
    }


    @Override
    public ContactDto editContact(Long id, ContactCreateDto contactDto) {
        ContactEntity entity = repository.findById(id).orElseThrow();
        entity.setEmail(contactDto.getEmail());
        entity.setName(contactDto.getName());
        entity.setGroups(groupRepository.findAllById(contactDto.getGroups()));

        return mapper.dto(repository.save(entity));
    }

    @Override
    public boolean deleteContacts(Long[] id) {
        repository.deleteAllById(Arrays.stream(id).toList());
        return true;
    }

}
