package net.artux.sendler.model.mailing;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import net.artux.sendler.entity.mailing.MailingEntity;
import net.artux.sendler.entity.statistic.MailingEventEntity;
import net.artux.sendler.entity.statistic.MailingResultEntity;
import net.artux.sendler.model.contact.ContactMapper;
import net.artux.sendler.model.settings.UserMailMapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ContactMapper.class, UserMailMapper.class})
public interface MailingMapper {

    MailingDto dto(MailingEntity entity);

    List<MailingDto> dto(List<MailingEntity> entity);

    @Mapping(target = "email", source = "event.contact.email")
    MailingEventDto dto(MailingEventEntity event);

    MailingResultDto dto(MailingResultEntity entity, long openings, long linkOpenings, long events);
}
