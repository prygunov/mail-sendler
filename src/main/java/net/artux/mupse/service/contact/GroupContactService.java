package net.artux.mupse.service.contact;

import net.artux.mupse.model.contact.ContactDto;
import net.artux.mupse.model.contact.ContactGroupCreateDto;
import net.artux.mupse.model.contact.ContactGroupDto;
import net.artux.mupse.model.contact.CreateContactDto;
import net.artux.mupse.model.contact.ParsingResult;
import net.artux.mupse.model.page.QueryPage;
import net.artux.mupse.model.page.ResponsePage;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

public interface GroupContactService {

    ParsingResult saveContactsFromFile(Long id, MultipartFile file) throws IOException;

    void exportContactsIn(Long id, Writer writer) throws IOException;

    ResponsePage<ContactDto> getContacts(Long id, QueryPage queryPage);

    ContactDto createContact(Long id, CreateContactDto dto);

    ResponsePage<ContactGroupDto> getGroups(QueryPage page);


    ContactGroupDto createGroup(ContactGroupCreateDto createDto);

    List<ContactDto> createContacts(Long id, List<CreateContactDto> dtos);

    boolean putContacts(Long id, List<Long> ids);
}
