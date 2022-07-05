package net.artux.mupse.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.artux.mupse.model.contact.ContactDto;
import net.artux.mupse.model.contact.CreateContactDto;
import net.artux.mupse.model.contact.ParsingResult;
import net.artux.mupse.service.contact.ContactService;
import net.artux.mupse.service.contact.SubscriptionService;
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
    public ContactDto subscribe(@Parameter(name = "Токен пользователя") @RequestParam UUID uuid, @RequestBody CreateContactDto dto) {
        return service.subscribe(uuid, dto);
    }

}
