package net.artux.sendler.model.enums;

import net.artux.sendler.entity.mailing.MailingStatus;
import net.artux.sendler.model.contact.ContactMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = ContactMapper.class)
public interface EnumMapper {

    @Mapping(target = "title", source = "status.name")
    @Mapping(target = "key", expression = "java(status.toString())")
    EnumDto dto(MailingStatus status);
    EnumDto[] dto(MailingStatus[] status);
}
