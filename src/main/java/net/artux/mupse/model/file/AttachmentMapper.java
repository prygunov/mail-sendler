package net.artux.mupse.model.file;

import net.artux.mupse.entity.file.AttachmentEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AttachmentMapper {

    AttachmentDto dto(AttachmentEntity entity);
    List<AttachmentDto> dto(List<AttachmentEntity> entity);

}
