package net.artux.sendler.service.contact;

import lombok.RequiredArgsConstructor;
import net.artux.sendler.entity.contact.ContactEntity;
import net.artux.sendler.model.contact.ContactCreateDto;
import net.artux.sendler.model.contact.ContactDto;
import net.artux.sendler.model.contact.ContactMapper;
import net.artux.sendler.model.contact.creation.ContactCreationResultDto;
import net.artux.sendler.model.contact.creation.ContactMassiveCreateDto;
import net.artux.sendler.model.contact.creation.CreationMapper;
import net.artux.sendler.model.exception.exceptions.NotFoundException;
import net.artux.sendler.model.page.QueryPage;
import net.artux.sendler.model.page.ResponsePage;
import net.artux.sendler.repository.contact.ContactGroupRepository;
import net.artux.sendler.repository.contact.ContactRepository;
import net.artux.sendler.repository.contact.TempContactRepository;
import net.artux.sendler.service.user.UserService;
import net.artux.sendler.service.util.PageService;
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
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContactServiceImpl implements ContactService {

    private final ContactRepository repository;
    private final ContactGroupRepository groupRepository;
    private final TempContactRepository tempRepository;
    private final ContactMapper mapper;
    private final CreationMapper creationMapper;

    private final UserService userService;
    private final ContactRegistrationService contactRegistrationService;
    private final PageService pageService;


    @Override
    public ContactCreationResultDto saveContactsFromFile(MultipartFile file) throws IOException {
        return creationMapper.dto(contactRegistrationService.contactsContactsFromFile(file));
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
        boolean searchingByAddress = search != null && search.contains("@");
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher(searchingByAddress ? "email" : "name", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
        Example<ContactEntity> example = searchingByAddress ? Example.of(new ContactEntity(userService.getUserEntity(), null, search), matcher) : Example.of(new ContactEntity(userService.getUserEntity(), search), matcher);
        Page<ContactEntity> contactEntities = repository.findAll(example, pageService.getPageable(queryPage));
        return pageService.mapDataPageToResponsePage(contactEntities, mapper::dto);
    }

    @Override
    public ContactCreationResultDto createContacts(List<ContactMassiveCreateDto> dtos) {
        return creationMapper.dto(contactRegistrationService.createContacts(dtos));
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
        return mapper.dto(getEntity(id));
    }

    @Override
    public ContactDto editContact(Long id, ContactCreateDto contactDto) {
        ContactEntity entity = getEntity(id);
        entity.setEmail(contactDto.getEmail());
        entity.setName(contactDto.getName());
        entity.setGroups(groupRepository.findAllByIdInAndOwner(contactDto.getGroups(), userService.getUserEntity()));

        return mapper.dto(repository.save(entity));
    }

    @Override
    public boolean deleteContacts(Long[] id) {
        repository.deleteAllById(Arrays.stream(id).toList());
        return true;
    }

    private ContactEntity getEntity(Long id){
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Контакт не найден"));
    }

}
