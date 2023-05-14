package net.artux.sendler.service.contact;

import org.springframework.web.multipart.MultipartFile;
import net.artux.sendler.entity.contact.ContactEntity;
import net.artux.sendler.model.contact.ContactCreateDto;
import net.artux.sendler.model.contact.ContactDto;
import net.artux.sendler.model.contact.creation.ContactCreationResultDto;
import net.artux.sendler.model.contact.creation.ContactMassiveCreateDto;
import net.artux.sendler.model.page.QueryPage;
import net.artux.sendler.model.page.ResponsePage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

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
