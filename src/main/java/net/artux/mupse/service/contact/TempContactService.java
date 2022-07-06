package net.artux.mupse.service.contact;

import lombok.RequiredArgsConstructor;
import net.artux.mupse.entity.contact.ContactEntity;
import net.artux.mupse.entity.contact.TempContactEntity;
import net.artux.mupse.entity.user.UserEntity;
import net.artux.mupse.model.contact.ContactContainer;
import net.artux.mupse.model.contact.ContactMapper;
import net.artux.mupse.repository.contact.ContactRepository;
import net.artux.mupse.repository.contact.TempContactRepository;
import net.artux.mupse.service.user.UserService;
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

    //фильтр пришедших контактов
    private ContactContainer getContainer(List<TempContactEntity> parsedContacts) {
        UserEntity userEntity = userService.getUserEntity();
        parsedContacts.removeIf(tempContactEntity -> StringUtils.isEmpty(tempContactEntity.getEmail())); // удаляем пустые
        for (TempContactEntity c : parsedContacts) {
            //задаем владельца и почту в lowercase
            c.setOwner(userEntity);
            c.setEmail(c.getEmail().toLowerCase());
        }
        tempContactRepository.saveAll(parsedContacts); // сохраняем временные контакты

        /*
        * ниже сначала выгружаются контакты, которые отсутствуют у юзера - они в дальнейшем будут сохранены
        * затем выгружаем контакты, которые уже есть у юзера, но при этом пришли с парсинга
        *
        */

        List<TempContactEntity> originalAccepted = tempContactRepository.getOriginalContacts(userEntity.getId());
        List<ContactEntity> collisionAccepted = repository.getCollisionContacts(userEntity.getId());

        List<ContactEntity> originals = originalAccepted.stream().map(mapper::entity).collect(Collectors.toList());
        repository.saveAll(originals);
        tempContactRepository.deleteAllByOwner(userEntity); // удаляем временные контакты, иначе будут мешать в следующих загрузках
        return new ContactContainer(originals, collisionAccepted, parsedContacts.size());
    }

    public ContactContainer detect(MultipartFile file) throws IOException {
        return getContainer(parseContacts(file));
    }

}
