package net.artux.mupse.service.settings;

import lombok.RequiredArgsConstructor;
import net.artux.mupse.entity.settings.UserMailEntity;
import net.artux.mupse.model.settings.UserMailCreateDto;
import net.artux.mupse.model.settings.UserMailDto;
import net.artux.mupse.model.settings.UserMailMapper;
import net.artux.mupse.repository.settings.UserMailRepository;
import net.artux.mupse.service.user.UserService;
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
    public UserMailDto editUserMail(Long id, UserMailCreateDto createDto) {
        UserMailEntity entity = repository.findByOwnerAndId(userService.getUserEntity(), id).orElseThrow();
        entity.setPort(createDto.getPort());
        entity.setHost(createDto.getHost());
        entity.setProtocol(createDto.getProtocol());

        entity.setUsername(createDto.getUsername());
        entity.setPassword(createDto.getPassword());

        return mapper.dto(repository.save(entity));
    }

    @Override
    public boolean deleteUserMailDto(Long id) {
        UserMailEntity entity = repository.findByOwnerAndId(userService.getUserEntity(), id).orElseThrow();
        repository.delete(entity);
        return true;
    }
}
