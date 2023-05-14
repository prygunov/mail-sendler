package net.artux.sendler.service.contact;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import net.artux.sendler.entity.contact.ContactEntity;
import net.artux.sendler.entity.contact.TempContactEntity;
import net.artux.sendler.entity.user.UserEntity;
import net.artux.sendler.model.contact.ContactCreateDto;
import net.artux.sendler.model.contact.creation.ContactCreationResult;
import net.artux.sendler.model.contact.creation.ContactMassiveCreateDto;
import net.artux.sendler.model.contact.creation.CreationMapper;
import net.artux.sendler.repository.contact.ContactRepository;
import net.artux.sendler.repository.contact.TempContactRepository;
import net.artux.sendler.service.user.UserService;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ContactRegistrationServiceImpl implements ContactRegistrationService {

    private final ContactRepository repository;
    private final TempContactRepository tempContactRepository;
    private final CreationMapper creationMapper;
    private final UserService userService;

    private List<ContactMassiveCreateDto> parseContacts(MultipartFile file) throws IOException {
        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);

        List<ContactMassiveCreateDto> result = new LinkedList<>();
        DataFormatter objDefaultFormat = new DataFormatter();
        for (Row row : sheet) {

            if (row.getCell(0) != null && row.getCell(1) != null) {
                String email = objDefaultFormat.formatCellValue(row.getCell(0));
                String name = objDefaultFormat.formatCellValue(row.getCell(1));

                if (!ObjectUtils.isEmpty(email)) {
                    ContactMassiveCreateDto contactEntity = new ContactMassiveCreateDto();
                    contactEntity.setEmail(email);
                    contactEntity.setName(name);

                    result.add(contactEntity);
                }
            }
        }

        return result;
    }

    @Override
    @Transactional
    public ContactCreationResult createContacts(List<ContactMassiveCreateDto> parsedContacts) {
        UserEntity owner = userService.getUserEntity();
        Set<TempContactEntity> tempContacts = creationMapper.tempEntity(parsedContacts, owner);
        tempContactRepository.saveAllAndFlush(tempContacts); // сохраняем временные контакты
        /*
         * ниже сначала выгружаются контакты, которые отсутствуют у юзера - они в дальнейшем будут сохранены
         * затем выгружаем контакты, которые уже есть у юзера, но при этом пришли с парсинга
         */

        int regexRejected = tempContactRepository.deleteNotValidContacts(owner.getId());
        int duplicateRejected = tempContactRepository.deleteDuplicateEmail(owner.getId());

        List<TempContactEntity> originalAccepted = tempContactRepository.getOriginalContacts(owner.getId());
        List<ContactEntity> collisionAccepted = repository.getCollisionContacts(owner.getId());

        List<ContactEntity> originals = creationMapper.entity(originalAccepted);

        originals = repository.saveAllAndFlush(originals);
        tempContactRepository.deleteAllByOwner(owner); // удаляем временные контакты, иначе будут мешать в следующих загрузках

        return ContactCreationResult.builder()
                .collisionContacts(collisionAccepted)
                .originalContacts(originals)
                .all(parsedContacts.size())
                .regexRejected(regexRejected)
                .duplicateRejected(duplicateRejected)
                .build();
    }

    @Override
    public ContactCreationResult contactsContactsFromFile(MultipartFile file) throws IOException {
        return createContacts(parseContacts(file));
    }

    @Override
    public ContactEntity createContactWithOwner(UserEntity user, ContactCreateDto dto) {
        if (repository.findByOwnerAndEmailIgnoreCase(userService.getUserEntity(), dto.getEmail()).isPresent())
            throw new RuntimeException("Контакт с таким адресом уже существует.");

        ContactEntity entity = new ContactEntity();
        entity.setName(dto.getName());
        entity.setEmail(dto.getEmail().toLowerCase());
        entity.setOwner(user);
        entity.setToken(UUID.randomUUID());
        return repository.save(entity);
    }

}
