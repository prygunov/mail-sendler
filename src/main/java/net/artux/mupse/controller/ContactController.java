package net.artux.mupse.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.artux.mupse.model.contact.ContactDto;
import net.artux.mupse.model.contact.CreateContactDto;
import net.artux.mupse.model.contact.ParsingResult;
import net.artux.mupse.model.page.QueryPage;
import net.artux.mupse.model.page.ResponsePage;
import net.artux.mupse.service.contact.ContactService;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Tag(name = "Контакты")
@RestController
@RequestMapping("/contacts")
public class ContactController {

    private final ContactService service;

    @Operation(summary = "Загрузка контактов csv-файлом")
    @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ParsingResult uploadContacts(@RequestPart(value = "file") final MultipartFile file) throws IOException {
        return service.saveContactsFromFile(file);
    }

    @Operation(summary = "Выгрузка контактов csv-файлом")
    @GetMapping("/export")
    public void allContacts(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.addHeader("Content-Disposition", "attachment; filename=\"contacts.csv\"");
        service.exportContacts(response.getWriter());
    }

    @Operation(summary = "Получение всех контактов страницами")
    @GetMapping
    public ResponsePage<ContactDto> getPageableContacts(@Valid @ParameterObject QueryPage queryPage) {
        return service.getContacts(queryPage);
    }

    @Operation(summary = "Ручное создание контактов")
    @PostMapping
    public List<ContactDto> createContact(@Valid @RequestBody List<CreateContactDto> contactDto) {
        return service.createContacts(contactDto);
    }

    @Operation(summary = "Изменение контакта (id в теле)")
    @PutMapping
    public ContactDto editContact(@Valid @RequestBody ContactDto contactDto) {
        return service.editContact(contactDto);
    }

    @Operation(summary = "Удаление контактов навсегда")
    @DeleteMapping
    public boolean deleteContact(@RequestBody Long[] id) {
        return service.deleteContacts(id);
    }

}
