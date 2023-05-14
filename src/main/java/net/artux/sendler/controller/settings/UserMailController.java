package net.artux.sendler.controller.settings;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.artux.sendler.model.settings.UserMailCreateDto;
import net.artux.sendler.model.settings.UserMailDto;
import net.artux.sendler.service.sender.SenderService;
import net.artux.sendler.service.settings.UserMailService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@Tag(name = "Адреса пользователя")
@RestController
@RequestMapping("/settings/mails")
public class UserMailController {

    private final UserMailService service;
    private final SenderService senderService;

    @Operation(summary = "Получить все адреса")
    @GetMapping
    public List<UserMailDto> getUserMails() {
        return service.getMails();
    }

    @Operation(summary = "Создать адрес")
    @PostMapping
    public UserMailDto createUserMail(@Valid @RequestBody UserMailCreateDto dto) {
        return service.createUserMail(dto);
    }

    @Operation(summary = "Получить адрес")
    @GetMapping("/{id}")
    public UserMailDto getUserMail(@PathVariable("id") Long id) {
        return service.getMail(id);
    }

    @Operation(summary = "Удалить адрес")
    @DeleteMapping("/{id}")
    public boolean editUserMail(@PathVariable("id") Long id) {
        return service.deleteUserMailDto(id);
    }

    @Operation(summary = "Протестировать адрес")
    @GetMapping("/test/{id}")
    public boolean getUserMails(@PathVariable("id") Long id) {
        return senderService.testMail(id);
    }

}
