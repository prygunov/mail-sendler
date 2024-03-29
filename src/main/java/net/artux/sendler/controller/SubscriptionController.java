package net.artux.sendler.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.artux.sendler.model.contact.ContactCreateDto;
import net.artux.sendler.model.contact.ContactDto;
import net.artux.sendler.service.contact.SubscriptionService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@Tag(name = "Управление подпиской")
@RestController
@RequestMapping("/subs")
public class SubscriptionController {

    private final SubscriptionService service;

    @Operation(summary = "Отписка контакта")
    @GetMapping(value = "/unsubscribe")
    public ContactDto unsubscribe(@RequestParam UUID uuid) {
        return service.unsubscribe(uuid);
    }

    @Operation(summary = "Повторная подписка контакта")
    @PostMapping(value = "/subscribe")
    public ContactDto subscribe(@RequestParam @Parameter(name = "Токен контакта") UUID uuid) {
        return service.subscribe(uuid);
    }

    @Operation(summary = "Первая подписка контакта")
    @PutMapping(value = "/subscribe")
    public ContactDto subscribe(@Parameter(name = "Токен пользователя") @RequestParam UUID uuid, @RequestBody ContactCreateDto dto) {
        return service.subscribe(uuid, dto);
    }

}
