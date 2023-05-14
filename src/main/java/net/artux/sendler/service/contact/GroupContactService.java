package net.artux.sendler.service.contact;

import org.springframework.web.multipart.MultipartFile;
import net.artux.sendler.model.contact.ContactDto;
import net.artux.sendler.model.contact.ContactGroupCreateDto;
import net.artux.sendler.model.contact.ContactGroupDto;
import net.artux.sendler.model.contact.creation.ContactCreationResultDto;
import net.artux.sendler.model.contact.creation.ContactMassiveCreateDto;
import net.artux.sendler.model.page.QueryPage;
import net.artux.sendler.model.page.ResponsePage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

public interface GroupContactService {

    ContactCreationResultDto saveContactsFromFile(Long id, MultipartFile file) throws IOException;

    ByteArrayInputStream exportContacts(Long id) throws IOException;

    ResponsePage<ContactDto> getContacts(Long id, QueryPage queryPage, String search);

    ResponsePage<ContactGroupDto> getGroups(QueryPage page, String search);

    List<ContactGroupDto> getGroups(String search);

    ContactGroupDto createGroup(ContactGroupCreateDto createDto);

    ContactGroupDto getGroup(Long id);

    ContactGroupDto editGroup(Long id, ContactGroupCreateDto createDto);

    boolean deleteGroup(Long id, boolean deleteContacts);

    ContactCreationResultDto createContacts(Long id, List<ContactMassiveCreateDto> dtos);

    boolean putContacts(Long id, List<Long> ids);

    boolean deleteContactsFromGroup(Long id, List<Long> ids);


}
