package net.artux.mupse.service.template;

import net.artux.mupse.model.page.QueryPage;
import net.artux.mupse.model.page.ResponsePage;
import net.artux.mupse.model.template.TemplateCreateDto;
import net.artux.mupse.model.template.TemplateDto;

public interface TemplateService {

    TemplateDto createTemplate(TemplateCreateDto createDto);

    TemplateDto editTemplate(Long id, TemplateCreateDto createDto);

    boolean deleteTemplate(Long id);

    ResponsePage<TemplateDto> getTemplates(QueryPage page, String search);

    TemplateDto getTemplate(Long id);
}
