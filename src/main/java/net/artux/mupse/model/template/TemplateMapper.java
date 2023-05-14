package net.artux.mupse.model.template;

import net.artux.mupse.entity.template.TemplateEntity;
import net.artux.mupse.entity.user.UserEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TemplateMapper {

    TemplateEntity entity(TemplateCreateDto dto, UserEntity owner);

    TemplateDto dto(TemplateEntity entity);
    List<TemplateDto> dto(List<TemplateEntity> entityList);

}
