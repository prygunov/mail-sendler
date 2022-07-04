package net.artux.mupse.service.contact;

import net.artux.mupse.model.contact.ContactDto;
import net.artux.mupse.model.contact.CreateContactDto;
import net.artux.mupse.model.contact.ParsingResult;
import net.artux.mupse.model.page.QueryPage;
import net.artux.mupse.model.page.ResponsePage;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

public interface ContactService {

    ParsingResult saveContactsFromFile(MultipartFile file) throws IOException;

    void exportContacts(Writer writer) throws IOException;

    ResponsePage<ContactDto> getContacts(QueryPage queryPage);

    ContactDto createContact(CreateContactDto dto);

    ContactDto editContact(ContactDto contactDto);
    boolean deleteContacts(Long[] id);

    List<ContactDto> createContacts(List<CreateContactDto> dtos);


}
