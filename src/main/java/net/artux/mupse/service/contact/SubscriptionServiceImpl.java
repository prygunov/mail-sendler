package net.artux.mupse.service.contact;

import lombok.RequiredArgsConstructor;
import net.artux.mupse.entity.contact.ContactEntity;
import net.artux.mupse.entity.user.UserEntity;
import net.artux.mupse.model.contact.ContactDto;
import net.artux.mupse.model.contact.ContactMapper;
import net.artux.mupse.model.contact.CreateContactDto;
import net.artux.mupse.repository.contact.ContactRepository;
import net.artux.mupse.repository.user.UserRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final ContactRepository repository;
    private final UserRepository userRepository;
    private final ContactMapper contactMapper;
    private final ContactService contactService;

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
    public ContactDto subscribe(UUID userUuid, CreateContactDto dto) {
        UserEntity user = userRepository.findByToken(userUuid).orElseThrow();
        return contactMapper.dto(contactService.createContactWithOwner(user, dto));
    }
}
