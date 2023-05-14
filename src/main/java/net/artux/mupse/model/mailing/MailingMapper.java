package net.artux.mupse.model.mailing;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import net.artux.mupse.entity.mailing.MailingEntity;
import net.artux.mupse.entity.statistic.MailingEventEntity;
import net.artux.mupse.entity.statistic.MailingResultEntity;
import net.artux.mupse.model.contact.ContactMapper;
import net.artux.mupse.model.settings.UserMailMapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ContactMapper.class, UserMailMapper.class})
public interface MailingMapper {

    MailingDto dto(MailingEntity entity);

    List<MailingDto> dto(List<MailingEntity> entity);

    @Mapping(target = "email", source = "event.contact.email")
    MailingEventDto dto(MailingEventEntity event);

    MailingResultDto dto(MailingResultEntity entity, long openings, long linkOpenings, long events);
}
