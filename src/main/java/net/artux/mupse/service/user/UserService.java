package net.artux.mupse.service.user;

import net.artux.mupse.entity.user.UserEntity;
import net.artux.mupse.model.user.RegisterUserDto;
import net.artux.mupse.model.user.UserDto;

public interface UserService {

    UserDto registerUser(RegisterUserDto registerUser);
    UserDto findByLogin(String login);
    UserDto getUser();
    UserEntity getUserEntity();

}
