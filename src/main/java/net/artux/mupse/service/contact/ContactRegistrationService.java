package net.artux.mupse.service.contact;

import org.springframework.web.multipart.MultipartFile;
import net.artux.mupse.entity.contact.ContactEntity;
import net.artux.mupse.entity.user.UserEntity;
import net.artux.mupse.model.contact.ContactCreateDto;
import net.artux.mupse.model.contact.creation.ContactCreationResult;
import net.artux.mupse.model.contact.creation.ContactMassiveCreateDto;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface ContactRegistrationService {

    ContactCreationResult createContacts(List<ContactMassiveCreateDto> contacts);

    ContactCreationResult contactsContactsFromFile(MultipartFile file) throws IOException;

    ContactEntity createContactWithOwner(UserEntity user, ContactCreateDto dto);
}

