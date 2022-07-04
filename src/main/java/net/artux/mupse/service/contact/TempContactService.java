package net.artux.mupse.service.contact;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import net.artux.mupse.entity.contact.ContactEntity;
import net.artux.mupse.entity.contact.TempContactEntity;
import net.artux.mupse.entity.user.UserEntity;
import net.artux.mupse.model.contact.ContactContainer;
import net.artux.mupse.model.contact.ContactMapper;
import net.artux.mupse.repository.contact.ContactRepository;
import net.artux.mupse.repository.contact.TempContactRepository;
import net.artux.mupse.service.user.UserService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TempContactService {

    private final ContactRepository repository;
    private final TempContactRepository tempContactRepository;
    private final ContactMapper mapper;

    private final UserService userService;
    private final CSVParser parser = new CSVParserBuilder().withSeparator(';').build();

    public List<TempContactEntity> parseContacts(MultipartFile file) throws IOException {
        Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));

        CSVReader s = new CSVReaderBuilder(reader).withCSVParser(parser).build();
        CsvToBean<TempContactEntity> csvToBean = new CsvToBeanBuilder<TempContactEntity>(s)
                .withType(TempContactEntity.class)
                .withIgnoreLeadingWhiteSpace(true)
                .build();

        return csvToBean.parse();
    }

    private ContactContainer getContainer(List<TempContactEntity> parsedContacts) {
        UserEntity userEntity = userService.getUserEntity();
        for (TempContactEntity c : parsedContacts) {
            c.setOwner(userEntity);
            c.setEmail(c.getEmail().toLowerCase());
        }

        tempContactRepository.saveAll(parsedContacts);

        List<TempContactEntity> originalAccepted = tempContactRepository.getOriginalContacts(userEntity.getId());
        List<ContactEntity> collisionAccepted = repository.getCollisionContacts(userEntity.getId());

        List<ContactEntity> originals = originalAccepted.stream().map(mapper::entity).collect(Collectors.toList());
        repository.saveAll(originals);
        return new ContactContainer(originals, collisionAccepted, parsedContacts.size());
    }

    public ContactContainer detect(MultipartFile file) throws IOException {
        return getContainer(parseContacts(file));
    }

}
