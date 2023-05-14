package net.artux.mupse.controller.contact;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import net.artux.mupse.model.contact.ContactCreateDto;
import net.artux.mupse.model.contact.ContactDto;
import net.artux.mupse.model.reason.ReasonModel;
import net.artux.mupse.service.contact.SubscriptionService;
import net.artux.mupse.service.reason.ReasonService;

import javax.validation.Valid;
import java.util.UUID;

@RequiredArgsConstructor
@Tag(name = "Управление подпиской")
@Controller
@RequestMapping("/subs")
public class SubscriptionController {

    private final SubscriptionService service;
    private final ReasonService reasonService;

    @Operation(summary = "Отписка контакта")
    @GetMapping(value = "/unsubscribe/{uuid}")
    public String unsubscribe(@PathVariable UUID uuid, Model model) {
        service.unsubscribe(uuid);
        reasonService.createReason(uuid);
        model.addAttribute("token", uuid.toString());
        return "unsubscribe";
    }

    @Operation(summary = "Указание причины отписки")
    @PostMapping(value = "/unsubscribe/{uuid}")
    public String selectReasonContent(@PathVariable UUID uuid, @Valid @ModelAttribute ReasonModel reasonModel) {
        String content = reasonModel.getReason();
        if (content.equalsIgnoreCase("Другое"))
            content = reasonModel.getContent();
        reasonService.setReasonContent(uuid, content);
        return "afterReason";
    }

    @Operation(summary = "Повторная подписка контакта")
    @PostMapping(value = "/subscribe/{uuid}")
    public ContactDto subscribe(@PathVariable UUID uuid) {
        return service.subscribe(uuid);
    }

    @Operation(summary = "Подписать контакт на пользователя")
    @PutMapping(value = "/subscribe")
    public ContactDto subscribe(@Parameter(name = "Токен пользователя") @RequestParam UUID uuid, @RequestBody ContactCreateDto dto) {
        return service.subscribe(uuid, dto);
    }

}
