package net.artux.sendler.controller.mailing;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.artux.sendler.entity.mailing.MailingStatus;
import net.artux.sendler.model.enums.EnumDto;
import net.artux.sendler.model.enums.EnumMapper;
import net.artux.sendler.model.mailing.MailingCreateDto;
import net.artux.sendler.model.mailing.MailingDto;
import net.artux.sendler.model.mailing.TimeObject;
import net.artux.sendler.model.page.QueryPage;
import net.artux.sendler.model.page.ResponsePage;
import net.artux.sendler.service.mailing.MailingService;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@Tag(name = "Рассылки")
@RequiredArgsConstructor
@RestController
@RequestMapping("/mailings")
public class MailingController {

    private final MailingService mailingService;
    private final EnumMapper enumMapper;

    @Operation(summary = "Статусы рассылок")
    @GetMapping("/statuses")
    public EnumDto[] getMailingTypes() {
        return enumMapper.dto(MailingStatus.values());
    }

    @Operation(summary = "Получение рассылок")
    @GetMapping
    public ResponsePage<MailingDto> getMailings(@Valid @ParameterObject QueryPage queryPage, MailingStatus status) {
        return mailingService.getMailings(queryPage, status);
    }

    @Operation(summary = "Создание черновика рассылки")
    @PostMapping
    public MailingDto createMailing(@Valid @RequestBody MailingCreateDto createDto) {
        return mailingService.createMailing(createDto);
    }

    @Operation(summary = "Изменение черновика")
    @PutMapping("/{id}")
    public MailingDto editMailing(@PathVariable("id") Long id, @Valid @RequestBody MailingCreateDto createDto) {
        return mailingService.editMailing(id, createDto);
    }

    @Operation(summary = "Получение рассылки")
    @GetMapping("/{id}")
    public MailingDto getMailing(@PathVariable("id") Long id) {
        return mailingService.getMailing(id);
    }

    @Operation(summary = "Удаление черновика")
    @DeleteMapping("/{id}")
    public boolean deleteMailing(@PathVariable("id") Long id) {
        return mailingService.deleteMailingDraft(id);
    }

    @Operation(summary = "Старт рассылки")
    @PostMapping("/start/{id}")
    public MailingDto startMailing(@PathVariable("id") Long id) {
        return mailingService.prepareMailing(id);
    }

    @Operation(summary = "Установка отложенной рассылки")
    @PostMapping("/queue/{id}")
    public MailingDto openMailing(@PathVariable("id") Long id, @Valid @RequestBody TimeObject timeObject) {
        return mailingService.addMailingToQueue(id, timeObject.getTime());
    }

    @Operation(summary = "Отмена отложенной рассылки")
    @DeleteMapping("/queue/{id}")
    public MailingDto cancelMailing(@PathVariable("id") Long id) {
        return mailingService.removeMailingFromQueue(id);
    }
}
