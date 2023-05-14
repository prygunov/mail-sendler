package net.artux.sendler.model.contact.creation;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import net.artux.sendler.entity.contact.ContactEntity;
import net.artux.sendler.entity.contact.TempContactEntity;
import net.artux.sendler.entity.user.UserEntity;

import java.util.*;

@Mapper(componentModel = "spring", imports = UUID.class)
public interface CreationMapper {

    @Mapping(target = "name", source = "createDto.name")
    @Mapping(target = "email", expression = "java(createDto.getEmail().toLowerCase())")
    TempContactEntity tempEntity(ContactMassiveCreateDto createDto, UserEntity owner);

    default Set<TempContactEntity> tempEntity(List<ContactMassiveCreateDto> createDto, UserEntity owner) {
        Set<TempContactEntity> result = new HashSet<>();
        for (ContactMassiveCreateDto dto : createDto) {
            result.add(tempEntity(dto, owner));
        }
        return result;
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "disabled", ignore = true)
    @Mapping(target = "groups", ignore = true)
    @Mapping(target = "token", expression = "java(UUID.randomUUID())")
    ContactEntity entity(TempContactEntity tempContact);

    List<ContactEntity> entity(List<TempContactEntity> tempContacts);

    @Mapping(target = "collisionRejected", expression = "java(result.getCollisionContacts().size())")
    @Mapping(target = "accepted", expression = "java(result.getOriginalContacts().size())")
    ContactCreationResultDto dto(ContactCreationResult result);
}
