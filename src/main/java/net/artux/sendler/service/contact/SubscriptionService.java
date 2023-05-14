package net.artux.sendler.service.contact;

import net.artux.sendler.model.contact.ContactDto;
import net.artux.sendler.model.contact.ContactCreateDto;

import java.util.UUID;

public interface SubscriptionService {

    ContactDto subscribe(UUID contactUuid);

    ContactDto unsubscribe(UUID contactUuid);

    ContactDto subscribe(UUID userUuid, ContactCreateDto dto);
}
