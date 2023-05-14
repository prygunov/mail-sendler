package net.artux.sendler.model.reason;

import org.mapstruct.Mapper;
import net.artux.sendler.entity.reason.ReasonEntity;
import net.artux.sendler.model.contact.ContactMapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ContactMapper.class})
public interface ReasonMapper {

    ReasonDto dto(ReasonEntity entity);

    List<ReasonDto> dto(List<ReasonEntity> entity);
}
