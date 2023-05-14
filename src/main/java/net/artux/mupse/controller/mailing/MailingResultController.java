package net.artux.mupse.controller.mailing;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;
import net.artux.mupse.entity.statistic.EventType;
import net.artux.mupse.model.contact.ContactEventDto;
import net.artux.mupse.model.mailing.MailingEventDto;
import net.artux.mupse.model.mailing.MailingResultDto;
import net.artux.mupse.model.page.QueryPage;
import net.artux.mupse.model.page.ResponsePage;
import net.artux.mupse.service.mailing.MailingResultService;
import net.artux.mupse.service.statistic.StatisticService;

import javax.validation.Valid;


@Tag(name = "Результат рассылки")
@RequiredArgsConstructor
@RestController
@RequestMapping("/mailings/{id}/result")
public class MailingResultController {

    private final MailingResultService service;
    private final StatisticService statisticService;

    @Operation(summary = "Получение результата рассылки")
    @GetMapping
    public MailingResultDto getMailingResult(@PathVariable("id") Long id) {
        return service.getResult(id);
    }

    @Operation(summary = "Получение событий (ошибок) рассылки")
    @GetMapping("/events")
    public ResponsePage<MailingEventDto> getMailingEvents(@Valid @ParameterObject QueryPage queryPage, @PathVariable("id") Long id, @RequestParam(required = false) String search) {
        return service.getEvents(id, queryPage, search);
    }

    @Operation(summary = "Получение информации о событиях пользователей")
    @GetMapping("/contacts/events")
    public ResponsePage<ContactEventDto> getContactEvents(@PathVariable("id") Long id, @Valid @ParameterObject QueryPage queryPage, EventType type) {
        return statisticService.getEvents(id, queryPage, type);
    }

}
