package net.artux.mupse.service.settings;

import net.artux.mupse.model.settings.UserMailCreateDto;
import net.artux.mupse.model.settings.UserMailDto;

import java.util.List;

public interface UserMailService {

    List<UserMailDto> getMails();
    UserMailDto createUserMail(UserMailCreateDto createDto);
    UserMailDto getMail(Long id);
    UserMailDto editUserMail(Long id, UserMailCreateDto createDto);
    boolean deleteUserMailDto(Long id);

}
