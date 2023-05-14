package net.artux.sendler.service.contact;

import lombok.RequiredArgsConstructor;
import net.artux.sendler.entity.contact.ContactEntity;
import net.artux.sendler.entity.user.UserEntity;
import net.artux.sendler.model.contact.ContactDto;
import net.artux.sendler.model.contact.ContactMapper;
import net.artux.sendler.model.contact.ContactCreateDto;
import net.artux.sendler.repository.contact.ContactRepository;
import net.artux.sendler.repository.user.UserRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final ContactRepository repository;
    private final UserRepository userRepository;
    private final ContactMapper contactMapper;
    private final ContactRegistrationService registrationService;

    @Override
    public ContactDto subscribe(UUID contactUuid) {
        ContactEntity entity = repository.findByToken(contactUuid).orElseThrow();
        entity.setDisabled(false);

        return contactMapper.dto(repository.save(entity));
    }

    @Override
    public ContactDto unsubscribe(UUID contactUuid) {
        ContactEntity entity = repository.findByToken(contactUuid).orElseThrow();
        entity.setDisabled(true);

        return contactMapper.dto(repository.save(entity));
    }

    @Override
    public ContactDto subscribe(UUID userUuid, ContactCreateDto dto) {
        UserEntity user = userRepository.findByToken(userUuid).orElseThrow();
        return contactMapper.dto(registrationService.createContactWithOwner(user, dto));
    }
}
