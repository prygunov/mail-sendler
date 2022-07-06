package net.artux.mupse.service.contact;

import net.artux.mupse.model.contact.*;
import net.artux.mupse.model.page.QueryPage;
import net.artux.mupse.model.page.ResponsePage;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

public interface GroupContactService {

    ParsingResult saveContactsFromFile(Long id, MultipartFile file) throws IOException;

    ByteArrayInputStream exportContacts(Long id) throws IOException;

    ResponsePage<ContactDto> getContacts(Long id, QueryPage queryPage, String search);

    ResponsePage<ContactGroupDto> getGroups(QueryPage page, String search);
    ContactGroupDto createGroup(ContactGroupCreateDto createDto);
    ContactGroupDto getGroup(Long id);
    ContactGroupDto editGroup(Long id, ContactGroupCreateDto createDto);
    boolean deleteGroup(Long id, boolean deleteContacts);

    ContactDto createContact(Long id, ContactCreateDto dto);
    List<ContactDto> createContacts(Long id, List<ContactCreateDto> dtos);
    int putContacts(Long id, List<Long> ids);
    boolean deleteContactsFromGroup(Long id, List<Long> ids);


}
