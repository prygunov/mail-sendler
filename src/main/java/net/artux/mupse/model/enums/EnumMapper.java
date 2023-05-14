package net.artux.mupse.model.enums;

import net.artux.mupse.entity.mailing.MailingStatus;
import net.artux.mupse.model.contact.ContactMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = ContactMapper.class)
public interface EnumMapper {

    @Mapping(target = "title", source = "status.name")
    @Mapping(target = "key", expression = "java(status.toString())")
    EnumDto dto(MailingStatus status);
    EnumDto[] dto(MailingStatus[] status);
}
