package net.artux.sendler.service.settings;

import lombok.RequiredArgsConstructor;
import net.artux.sendler.entity.settings.UserMailEntity;
import net.artux.sendler.model.settings.UserMailCreateDto;
import net.artux.sendler.model.settings.UserMailDto;
import net.artux.sendler.model.settings.UserMailMapper;
import net.artux.sendler.repository.settings.UserMailRepository;
import net.artux.sendler.service.user.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserMailServiceImpl implements UserMailService {

    private final UserService userService;
    private final UserMailRepository repository;
    private final UserMailMapper mapper;

    @Override
    public List<UserMailDto> getMails() {
        return mapper.dto(repository.findAllByOwner(userService.getUserEntity()));
    }

    @Override
    public UserMailDto createUserMail(UserMailCreateDto createDto) {
        UserMailEntity entity = mapper.entity(createDto, userService.getUserEntity());
        return mapper.dto(repository.save(entity));
    }

    @Override
    public UserMailDto getMail(Long id) {
        return mapper.dto(repository.findByOwnerAndId(userService.getUserEntity(), id).orElseThrow());
    }

    @Override
    public boolean deleteUserMailDto(Long id) {
        UserMailEntity entity = repository.findByOwnerAndId(userService.getUserEntity(), id).orElseThrow();
        repository.delete(entity);
        return true;
    }
}
