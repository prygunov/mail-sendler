package net.artux.mupse.service.contact;

import com.opencsv.CSVWriter;
import lombok.RequiredArgsConstructor;
import net.artux.mupse.entity.contact.ContactEntity;
import net.artux.mupse.model.contact.*;
import net.artux.mupse.model.page.QueryPage;
import net.artux.mupse.model.page.ResponsePage;
import net.artux.mupse.repository.contact.ContactRepository;
import net.artux.mupse.repository.contact.TempContactRepository;
import net.artux.mupse.service.user.UserService;
import net.artux.mupse.service.util.PageService;
import net.artux.mupse.service.util.SortService;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.io.output.WriterOutputStream;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContactServiceImpl implements ContactService {

    private final ContactRepository repository;
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
    public ByteArrayInputStream exportContacts() throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("contacts");

        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
        headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue("email");
        cell.setCellStyle(headerCellStyle);

        cell = row.createCell(1);
        cell.setCellValue("name");
        cell.setCellStyle(headerCellStyle);

        List<ContactEntity> contactEntities = repository.findAllByOwner(userService.getUserEntity());
        for (int i = 0; i<contactEntities.size(); i++) {
            Row header = sheet.createRow(1 + i);
            ContactEntity contactEntity = contactEntities.get(i);
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
        Page<ContactEntity> contactEntities = repository.findAllByOwnerAndNameContainingIgnoreCase(userService.getUserEntity(), search, sortService.getSortInfo(ContactDto.class, queryPage, "name"));
        return pageService.mapDataPageToResponsePage(contactEntities, mapper.dto(contactEntities.getContent()));
    }

    ContactEntity createContactEntity(CreateContactDto dto) {
        if (repository.findByOwnerAndEmailIgnoreCase(userService.getUserEntity(), dto.getEmail()).isPresent())
            throw new RuntimeException("Контакт с таким адресом уже существует.");

        ContactEntity entity = new ContactEntity();
        entity.setName(dto.getName());
        entity.setEmail(dto.getEmail());
        entity.setOwner(userService.getUserEntity());
        return repository.save(entity);
    }

    @Override
    public ContactDto createContact(CreateContactDto dto) {
        return mapper.dto(createContactEntity(dto));
    }

    @Override
    public List<ContactDto> createContacts(List<CreateContactDto> dtos) {
        List<ContactDto> result = new LinkedList<>();
        for (CreateContactDto contactDto : dtos) {
            try {
                result.add(mapper.dto(createContactEntity(contactDto)));
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
