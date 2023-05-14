package net.artux.sendler.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.artux.sendler.model.contact.ContactCreateDto;
import net.artux.sendler.model.contact.ContactDto;
import net.artux.sendler.model.contact.creation.ContactCreationResultDto;
import net.artux.sendler.model.contact.creation.ContactMassiveCreateDto;
import net.artux.sendler.model.page.QueryPage;
import net.artux.sendler.model.page.ResponsePage;
import net.artux.sendler.service.contact.ContactService;
import org.apache.commons.io.IOUtils;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Tag(name = "Контакты")
@RestController
@RequestMapping("/contacts")
public class ContactController {

    private final ContactService service;

    @Operation(summary = "Загрузка контактов xlsx-файлом")
    @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ContactCreationResultDto uploadContacts(@RequestPart(value = "file") final MultipartFile file) throws IOException {
        return service.saveContactsFromFile(file);
    }

    @Operation(summary = "Выгрузка контактов xlsx-файлом")
    @GetMapping("/export")
    public void allContacts(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        response.addHeader("Content-Disposition", "attachment; filename=\"contacts.xlsx\"");

        ByteArrayInputStream stream = service.exportAllContacts();
        IOUtils.copy(stream, response.getOutputStream());
    }

    @Operation(summary = "Получение всех контактов страницами")
    @GetMapping
    public ResponsePage<ContactDto> getPageableContacts(@Valid @ParameterObject QueryPage queryPage, @RequestParam(required = false) String search) {
        return service.getContacts(queryPage, search);
    }

    @Operation(summary = "Поулчение контакта по id")
    @GetMapping("/{id}")
    public ContactDto getContact(@PathVariable("id") Long id) {
        return service.getContact(id);
    }

    @Operation(summary = "Ручное создание контактов")
    @PostMapping
    public ContactCreationResultDto createContact(@Valid @RequestBody List<ContactMassiveCreateDto> contactDto) {
        return service.createContacts(contactDto);
    }

    @Operation(summary = "Изменение контакта")
    @PutMapping("/{id}")
    public ContactDto editContact(@PathVariable("id") Long id, @Valid @RequestBody ContactCreateDto contactDto) {
        return service.editContact(id, contactDto);
    }

    @Operation(summary = "Удаление контактов навсегда")
    @DeleteMapping
    public boolean deleteContact(@RequestBody Long[] id) {
        return service.deleteContacts(id);
    }

    @Operation(summary = "Удаление всех контактов", description = "Дополнительно очишаются временные контакты.")
    @DeleteMapping("/all")
    public boolean deleteContact() {
        return service.deleteAllContacts();
    }

}
