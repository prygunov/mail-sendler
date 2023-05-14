package net.artux.mupse.model.contact;

import net.artux.mupse.entity.contact.ContactEntity;
import net.artux.mupse.entity.contact.ContactGroupEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import net.artux.mupse.entity.statistic.ContactEventEntity;

import java.util.List;
import java.util.UUID;

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

    ContactEventDto dto(ContactEventEntity entity);

    default String map(UUID uuid){
        return uuid.toString();
    }

}
