package net.artux.sendler.model.template;

import net.artux.sendler.entity.template.TemplateEntity;
import net.artux.sendler.entity.user.UserEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TemplateMapper {

    TemplateEntity entity(TemplateCreateDto dto, UserEntity owner);

    TemplateDto dto(TemplateEntity entity);
    List<TemplateDto> dto(List<TemplateEntity> entityList);

}
