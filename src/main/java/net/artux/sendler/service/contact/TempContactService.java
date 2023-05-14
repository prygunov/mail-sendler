package net.artux.sendler.service.contact;

import lombok.RequiredArgsConstructor;
import net.artux.sendler.entity.contact.ContactEntity;
import net.artux.sendler.entity.contact.TempContactEntity;
import net.artux.sendler.entity.user.UserEntity;
import net.artux.sendler.model.contact.ContactContainer;
import net.artux.sendler.model.contact.ContactMapper;
import net.artux.sendler.repository.contact.ContactRepository;
import net.artux.sendler.repository.contact.TempContactRepository;
import net.artux.sendler.service.user.UserService;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TempContactService {

    private final ContactRepository repository;
    private final TempContactRepository tempContactRepository;
    private final ContactMapper mapper;
    private final UserService userService;

    public List<TempContactEntity> parseContacts(MultipartFile file) throws IOException {
        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);

        List<TempContactEntity> result = new LinkedList<>();
        for (Row row : sheet) {
            if (row.getCell(0) != null && row.getCell(1) != null) {
                String email = row.getCell(0).getStringCellValue();
                String name = row.getCell(1).getStringCellValue();

                if (!StringUtils.isEmpty(email)) {
                    TempContactEntity contactEntity = new TempContactEntity();
                    contactEntity.setEmail(email);
                    contactEntity.setName(name);

                    result.add(contactEntity);
                }
            }
        }

        return result;
    }

    private ContactContainer getContainer(List<TempContactEntity> parsedContacts) {
        UserEntity userEntity = userService.getUserEntity();
        parsedContacts.removeIf(tempContactEntity -> StringUtils.isEmpty(tempContactEntity.getEmail()));
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
