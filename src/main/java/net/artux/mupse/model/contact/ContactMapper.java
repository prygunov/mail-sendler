package net.artux.mupse.model.contact;

import net.artux.mupse.entity.contact.ContactEntity;
import net.artux.mupse.entity.contact.ContactGroupEntity;
import net.artux.mupse.entity.contact.TempContactEntity;
import org.hibernate.annotations.Target;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import javax.persistence.ManyToMany;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ContactMapper {

    @Mapping(target = "contacts", expression = "java(entity.getContacts().size())")
    ContactGroupDto groupDto(ContactGroupEntity entity);
    List<ContactGroupDto> groupDto(List<ContactGroupEntity> groupEntities);

    ContactDto dto(ContactEntity entity);
    List<ContactDto> dto(List<ContactEntity> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "disabled", ignore = true)
    ContactEntity entity(TempContactEntity tempContactEntity);

}
