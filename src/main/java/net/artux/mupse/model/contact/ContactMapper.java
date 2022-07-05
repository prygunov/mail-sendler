package net.artux.mupse.model.contact;

import net.artux.mupse.entity.contact.ContactEntity;
import net.artux.mupse.entity.contact.ContactGroupEntity;
import net.artux.mupse.entity.contact.TempContactEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ContactMapper {

    @Named("countContacts")
    default int countContacts(ContactGroupEntity entity) {
        if (entity.getContacts() != null)
            return entity.getContacts().size();
        else return 0;
    }

    @Mapping(target = "contacts", source = "entity", qualifiedByName = "countContacts")
    ContactGroupDto groupDto(ContactGroupEntity entity);

    List<ContactGroupDto> groupDto(List<ContactGroupEntity> groupEntities);


    ContactDto dto(ContactEntity entity);

    List<ContactDto> dto(List<ContactEntity> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "disabled", ignore = true)
    @Mapping(target = "groups", ignore = true)
    @Mapping(target = "token", ignore = true)
    ContactEntity entity(TempContactEntity tempContactEntity);

}
