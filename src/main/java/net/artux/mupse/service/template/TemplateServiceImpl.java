package net.artux.mupse.service.template;

import lombok.RequiredArgsConstructor;
import net.artux.mupse.entity.template.TemplateEntity;
import net.artux.mupse.model.page.QueryPage;
import net.artux.mupse.model.page.ResponsePage;
import net.artux.mupse.model.template.TemplateCreateDto;
import net.artux.mupse.model.template.TemplateDto;
import net.artux.mupse.model.template.TemplateMapper;
import net.artux.mupse.repository.template.TemplateRepository;
import net.artux.mupse.service.user.UserService;
import net.artux.mupse.service.util.PageService;
import net.artux.mupse.service.util.SortService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TemplateServiceImpl implements TemplateService {

    private final UserService userService;
    private final SortService sortService;
    private final PageService pageService;

    private final TemplateRepository templateRepository;
    private final TemplateMapper mapper;

    @Override
    public TemplateDto createTemplate(TemplateCreateDto createDto) {
        TemplateEntity entity = mapper.entity(createDto, userService.getUserEntity());
        return mapper.dto(templateRepository.save(entity));
    }

    @Override
    public TemplateDto editTemplate(Long id, TemplateCreateDto createDto) {
        TemplateEntity entity = templateRepository.findByOwnerAndId(userService.getUserEntity(), id).orElseThrow();
        entity.setSubject(createDto.getSubject());
        entity.setContent(createDto.getContent());

        return mapper.dto(templateRepository.save(entity));
    }

    @Override
    public boolean deleteTemplate(Long id) {
        TemplateEntity entity = templateRepository.findByOwnerAndId(userService.getUserEntity(), id).orElseThrow();
        templateRepository.save(entity);
        return true;
    }

    @Override
    public ResponsePage<TemplateDto> getTemplates(QueryPage page, String search) {
        Page<TemplateEntity> entitiesPage = templateRepository.findAllByOwnerAndSubjectContainsIgnoreCase(userService.getUserEntity(), search,
                sortService.getSortInfo(TemplateDto.class, page, "subject"));
        return pageService.mapDataPageToResponsePage(entitiesPage, mapper.dto(entitiesPage.getContent()));
    }
}
