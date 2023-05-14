package net.artux.mupse.model.user;

import net.artux.mupse.entity.user.RoleType;
import net.artux.mupse.entity.user.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", imports = RoleType.class)
public interface UserMapper {

    @Mapping(target = "groups", ignore = true)
    @Mapping(target = "token", ignore = true)
    @Mapping(target = "role", expression = "java(RoleType.USER)")
    UserEntity entity(RegisterUserDto dto);

    UserDto dto(UserEntity userEntity);

}
