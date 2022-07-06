package net.artux.mupse.service.contact;

import net.artux.mupse.entity.contact.ContactEntity;
import net.artux.mupse.entity.user.UserEntity;
import net.artux.mupse.model.contact.ContactDto;
import net.artux.mupse.model.contact.ContactCreateDto;
import net.artux.mupse.model.contact.ParsingResult;
import net.artux.mupse.model.page.QueryPage;
import net.artux.mupse.model.page.ResponsePage;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

public interface ContactService {

    ParsingResult saveContactsFromFile(MultipartFile file) throws IOException;

    ByteArrayInputStream exportAllContacts() throws IOException;

    ResponsePage<ContactDto> getContacts(QueryPage queryPage, String search);

    ContactEntity createContactWithOwner(UserEntity user, ContactCreateDto dto);

    ContactDto editContact(Long id, ContactCreateDto contactDto);

    boolean deleteContacts(Long[] id);

    List<ContactDto> createContacts(List<ContactCreateDto> dtos);

    boolean deleteAllContacts();

    ContactDto getContact(Long id);

    ByteArrayInputStream exportContacts(List<ContactEntity> allByGroupAndOwner) throws IOException;

}
