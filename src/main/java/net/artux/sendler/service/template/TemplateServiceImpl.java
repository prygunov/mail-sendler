package net.artux.sendler.service.template;

import lombok.RequiredArgsConstructor;
import net.artux.sendler.entity.template.TemplateEntity;
import net.artux.sendler.model.exception.exceptions.NotFoundException;
import net.artux.sendler.model.page.QueryPage;
import net.artux.sendler.model.page.ResponsePage;
import net.artux.sendler.model.template.TemplateCreateDto;
import net.artux.sendler.model.template.TemplateDto;
import net.artux.sendler.model.template.TemplateMapper;
import net.artux.sendler.repository.template.TemplateRepository;
import net.artux.sendler.service.user.UserService;
import net.artux.sendler.service.util.PageService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TemplateServiceImpl implements TemplateService {

    private final UserService userService;
    private final PageService pageService;

    private final TemplateRepository templateRepository;
    private final TemplateMapper mapper;

    @Override
    public TemplateDto createTemplate(TemplateCreateDto createDto) {
        if (templateRepository.findByOwnerAndTitle(userService.getUserEntity(), createDto.getTitle()).isPresent())
            throw new RuntimeException("Шаблон с таким именем уже существует");
        TemplateEntity entity = mapper.entity(createDto, userService.getUserEntity());
        return mapper.dto(templateRepository.save(entity));
    }

    @Override
    public TemplateDto editTemplate(Long id, TemplateCreateDto createDto) {
        TemplateEntity entity = templateRepository.findByOwnerAndId(userService.getUserEntity(), id).orElseThrow(() -> new NotFoundException("Шаблон не найден"));
        entity.setTitle(createDto.getTitle());
        entity.setSubject(createDto.getSubject());
        entity.setContent(createDto.getContent());

        return mapper.dto(templateRepository.save(entity));
    }

    @Override
    public boolean deleteTemplate(Long id) {
        TemplateEntity entity = templateRepository.findByOwnerAndId(userService.getUserEntity(), id)
                .orElseThrow(() -> new NotFoundException("Шаблон не найден"));
        templateRepository.delete(entity);
        return true;
    }

    @Override
    public ResponsePage<TemplateDto> getTemplates(QueryPage page, String search) {
        Page<TemplateEntity> entitiesPage;
        if (search == null)
            entitiesPage = templateRepository.findAllByOwner(userService.getUserEntity(), pageService.getPageable(page));
        else
            entitiesPage = templateRepository
                    .findAllByOwnerAndTitleContainsIgnoreCase(userService.getUserEntity(), search, pageService.getPageable(page));
        return pageService.mapDataPageToResponsePage(entitiesPage, mapper::dto);
    }

    @Override
    public TemplateDto getTemplate(Long id) {
        TemplateEntity entity = templateRepository.findByOwnerAndId(userService.getUserEntity(), id)
                .orElseThrow(() -> new NotFoundException("Шаблон не найден"));
        return mapper.dto(entity);
    }
}
