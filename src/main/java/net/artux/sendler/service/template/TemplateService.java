package net.artux.sendler.service.template;

import net.artux.sendler.model.page.QueryPage;
import net.artux.sendler.model.page.ResponsePage;
import net.artux.sendler.model.template.TemplateCreateDto;
import net.artux.sendler.model.template.TemplateDto;

public interface TemplateService {

    TemplateDto createTemplate(TemplateCreateDto createDto);

    TemplateDto editTemplate(Long id, TemplateCreateDto createDto);

    boolean deleteTemplate(Long id);

    ResponsePage<TemplateDto> getTemplates(QueryPage page, String search);

    TemplateDto getTemplate(Long id);
}
