package net.artux.mupse.service.contact;

import net.artux.mupse.model.contact.ContactDto;
import net.artux.mupse.model.contact.CreateContactDto;

import java.util.UUID;

public interface SubscriptionService {

    ContactDto subscribe(UUID contactUuid);

    ContactDto unsubscribe(UUID contactUuid);

    ContactDto subscribe(UUID userUuid, CreateContactDto dto);
}
