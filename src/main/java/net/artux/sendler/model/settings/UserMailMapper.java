package net.artux.sendler.model.settings;

import net.artux.sendler.entity.settings.UserMailEntity;
import net.artux.sendler.entity.user.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMailMapper {

    UserMailDto dto(UserMailEntity entity);

    List<UserMailDto> dto(List<UserMailEntity> entity);

    @Mapping(target = "password", source = "createDto.password")
    UserMailEntity entity(UserMailCreateDto createDto, UserEntity owner);

}
