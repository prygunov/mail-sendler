package net.artux.mupse.service.contact;

import org.springframework.web.multipart.MultipartFile;
import net.artux.mupse.entity.contact.ContactEntity;
import net.artux.mupse.model.contact.ContactCreateDto;
import net.artux.mupse.model.contact.ContactDto;
import net.artux.mupse.model.contact.creation.ContactCreationResultDto;
import net.artux.mupse.model.contact.creation.ContactMassiveCreateDto;
import net.artux.mupse.model.page.QueryPage;
import net.artux.mupse.model.page.ResponsePage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface ContactService {

    ContactCreationResultDto saveContactsFromFile(MultipartFile file) throws IOException;

    ByteArrayInputStream exportAllContacts() throws IOException;

    ResponsePage<ContactDto> getContacts(QueryPage queryPage, String search);

    ContactDto editContact(Long id, ContactCreateDto contactDto);

    boolean deleteContacts(Long[] id);

    ContactCreationResultDto createContacts(List<ContactMassiveCreateDto> dtos);

    boolean deleteAllContacts();

    ContactDto getContact(Long id);

    ByteArrayInputStream exportContacts(List<ContactEntity> allByGroupAndOwner) throws IOException;

}
