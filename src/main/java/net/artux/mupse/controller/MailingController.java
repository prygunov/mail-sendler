package net.artux.mupse.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.artux.mupse.entity.mailing.MailingType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "Рассылки")
@RequiredArgsConstructor
@RestController
@RequestMapping("/mailing")
public class MailingController {

    @Operation(summary = "Типы рассылок")
    @GetMapping("/types")
    public MailingType[] getMailingTypes() {
        return MailingType.values();
    }

}
