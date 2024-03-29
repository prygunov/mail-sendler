package net.artux.sendler.model.contact;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import net.artux.sendler.entity.statistic.EventType;

import java.time.LocalDateTime;

@Data
public class ContactEventDto {

    private Long id;
    private ContactDto contact;
    @JsonFormat(pattern = "dd.MM.yyyy HH:mm")
    private LocalDateTime time;
    private EventType type;

}
