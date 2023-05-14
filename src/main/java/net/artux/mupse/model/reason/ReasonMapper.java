package net.artux.mupse.model.reason;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import net.artux.mupse.entity.mailing.MailingEntity;
import net.artux.mupse.entity.reason.ReasonEntity;
import net.artux.mupse.entity.statistic.MailingEventEntity;
import net.artux.mupse.entity.statistic.MailingResultEntity;
import net.artux.mupse.model.contact.ContactMapper;
import net.artux.mupse.model.mailing.MailingDto;
import net.artux.mupse.model.mailing.MailingEventDto;
import net.artux.mupse.model.mailing.MailingResultDto;
import net.artux.mupse.model.settings.UserMailMapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ContactMapper.class})
public interface ReasonMapper {

    ReasonDto dto(ReasonEntity entity);

    List<ReasonDto> dto(List<ReasonEntity> entity);
}
