package net.artux.sendler.service.settings;

import net.artux.sendler.model.settings.UserMailCreateDto;
import net.artux.sendler.model.settings.UserMailDto;

import java.util.List;

public interface UserMailService {

    List<UserMailDto> getMails();

    UserMailDto createUserMail(UserMailCreateDto createDto);

    UserMailDto getMail(Long id);

    boolean deleteUserMailDto(Long id);

}
