package net.artux.mupse.controller.settings;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.artux.mupse.model.settings.UserMailCreateDto;
import net.artux.mupse.model.settings.UserMailDto;
import net.artux.mupse.service.settings.UserMailService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Tag(name = "Адреса пользователя")
@RestController
@RequestMapping("/settings/mails")
public class UserMailController {

    private final UserMailService service;

    @Operation(summary = "Получить все адреса")
    @GetMapping
    public List<UserMailDto> getUserMails() {
        return service.getMails();
    }

    @Operation(summary = "Создать адрес")
    @PostMapping
    public UserMailDto createUserMail(@RequestBody UserMailCreateDto dto) {
        return service.createUserMail(dto);
    }

    @Operation(summary = "Изменить адрес")
    @PutMapping("/{id}")
    public UserMailDto editUserMail(@PathVariable("id") Long id, @RequestBody UserMailCreateDto dto) {
        return service.editUserMail(id, dto);
    }

    @Operation(summary = "Удалить адрес")
    @DeleteMapping("/{id}")
    public boolean editUserMail(@PathVariable("id") Long id) {
        return service.deleteUserMailDto(id);
    }


}
