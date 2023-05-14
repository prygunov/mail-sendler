package net.artux.sendler.service.contact;

import org.springframework.web.multipart.MultipartFile;
import net.artux.sendler.entity.contact.ContactEntity;
import net.artux.sendler.entity.user.UserEntity;
import net.artux.sendler.model.contact.ContactCreateDto;
import net.artux.sendler.model.contact.creation.ContactCreationResult;
import net.artux.sendler.model.contact.creation.ContactMassiveCreateDto;

import java.io.IOException;
import java.util.List;

public interface ContactRegistrationService {

    ContactCreationResult createContacts(List<ContactMassiveCreateDto> contacts);

    ContactCreationResult contactsContactsFromFile(MultipartFile file) throws IOException;

    ContactEntity createContactWithOwner(UserEntity user, ContactCreateDto dto);
}

