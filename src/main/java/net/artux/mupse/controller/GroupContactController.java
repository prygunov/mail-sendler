package net.artux.mupse.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.artux.mupse.model.contact.*;
import net.artux.mupse.model.page.QueryPage;
import net.artux.mupse.model.page.ResponsePage;
import net.artux.mupse.service.contact.GroupContactService;
import org.apache.commons.io.IOUtils;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Tag(name = "Группы контактов")
@RestController
@RequestMapping("/contacts/groups")
public class GroupContactController {

    private final GroupContactService service;

    @Operation(summary = "Загрузка контактов в  группу xlsx-файлом")
    @PostMapping(value = "/{id}/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ParsingResult uploadContacts(@PathVariable("id") Long id, @RequestPart(value = "file") final MultipartFile file) throws IOException {
        return service.saveContactsFromFile(id, file);
    }

    @Operation(summary = "Выгрузка контактов группы xlsx-файлом")
    @GetMapping("/{id}/export")
    public void allContacts(@PathVariable("id") Long id, HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        response.addHeader("Content-Disposition", "attachment; filename=\"contacts.xlsx\"");

        ByteArrayInputStream stream = service.exportContacts(id);
        IOUtils.copy(stream, response.getOutputStream());
    }

    @Operation(summary = "Получение контактов группы страницами")
    @GetMapping("/{id}")
    public ResponsePage<ContactDto> getPageableContacts(@PathVariable("id") Long id, @Valid @ParameterObject QueryPage queryPage, @RequestParam(required = false) String search) {
        return service.getContacts(id, queryPage, search);
    }


    @Operation(summary = "Получение групп контактов")
    @GetMapping
    public ResponsePage<ContactGroupDto> getPageableGroups(@Valid @ParameterObject QueryPage queryPage, @RequestParam(required = false) String search) {
        return service.getGroups(queryPage, search);
    }

    @Operation(summary = "Создание группы")
    @PostMapping
    public ContactGroupDto createGroup(@Valid @RequestBody ContactGroupCreateDto dto) {
        return service.createGroup(dto);
    }

    @Operation(summary = "Информация о группе")
    @GetMapping("/{id}/info")
    public ContactGroupDto getGroup(@PathVariable("id") Long id) {
        return service.getGroup(id);
    }

    @Operation(summary = "Редактирование группы")
    @PutMapping("/{id}/edit")
    public ContactGroupDto putContacts(@PathVariable("id") Long id, @RequestBody @Valid ContactGroupCreateDto dto) {
        return service.editGroup(id, dto);
    }

    @Operation(summary = "Удалить группу")
    @DeleteMapping("/{id}/group")
    public boolean deleteGroup(@PathVariable("id") Long id, boolean deleteContacts) {
        return service.deleteGroup(id, deleteContacts);
    }

    @Operation(summary = "Создание контактов в группе")
    @PostMapping("/{id}")
    public List<ContactDto> createContacts(@PathVariable("id") Long id, @Valid List<ContactCreateDto> dto) {
        return service.createContacts(id, dto);
    }

    @Operation(summary = "Добавление контактов в группу")
    @PutMapping("/{id}")
    public int putContacts(@PathVariable("id") Long id, @RequestBody @Valid List<Long> ids) {
        return service.putContacts(id, ids);
    }

    @Operation(summary = "Удаление контактов из группы")
    @DeleteMapping("/{id}")
    public boolean deleteContacts(@PathVariable("id") Long id, @RequestBody @Valid List<Long> ids) {
        return service.deleteContactsFromGroup(id, ids);
    }

}
