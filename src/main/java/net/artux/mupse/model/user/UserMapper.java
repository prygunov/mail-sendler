package net.artux.mupse.model.user;

import net.artux.mupse.entity.user.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "groups", ignore = true)
    UserEntity entity(RegisterUserDto dto);

    UserDto dto(UserEntity userEntity);

}
