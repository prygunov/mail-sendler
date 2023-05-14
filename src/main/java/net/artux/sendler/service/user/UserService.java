package net.artux.sendler.service.user;

import net.artux.sendler.entity.user.UserEntity;
import net.artux.sendler.model.user.RegisterUserDto;
import net.artux.sendler.model.user.UserDto;

public interface UserService {

    UserDto registerUser(RegisterUserDto registerUser);
    UserDto findByLogin(String login);
    UserDto getUser();
    UserEntity getUserEntity();

}
