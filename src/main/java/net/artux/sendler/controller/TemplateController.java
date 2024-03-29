package net.artux.sendler.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.artux.sendler.model.page.QueryPage;
import net.artux.sendler.model.page.ResponsePage;
import net.artux.sendler.model.template.TemplateCreateDto;
import net.artux.sendler.model.template.TemplateDto;
import net.artux.sendler.service.template.TemplateService;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@Tag(name = "Шаблоны")
@RequiredArgsConstructor
@RestController
@RequestMapping("/template")
public class TemplateController {

    private final TemplateService templateService;

    @Operation(summary = "Создать шаблон")
    @PostMapping
    public TemplateDto createTemplate(@RequestBody TemplateCreateDto createDto) {
        return templateService.createTemplate(createDto);
    }

    @Operation(summary = "Изменить шаблон")
    @PutMapping("/{id}")
    public TemplateDto editTemplate(@PathVariable("id") Long id, @RequestBody TemplateCreateDto editDto) {
        return templateService.editTemplate(id, editDto);
    }

    @Operation(summary = "Удалить шаблон")
    @DeleteMapping("/{id}")
    public boolean deleteTemplate(@PathVariable("id") Long id) {
        return templateService.deleteTemplate(id);
    }

    @Operation(summary = "Получить шаблоны")
    @GetMapping
    public ResponsePage<TemplateDto> getTemplates(@ParameterObject @Valid QueryPage page,
                                                  @RequestParam(required = false) String search) {
        return templateService.getTemplates(page, search);
    }

}
