package net.artux.sendler.model.reason;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import net.artux.sendler.model.contact.ContactDto;

import java.time.LocalDateTime;

@Data
public class ReasonDto {

    private ContactDto contact;
    @JsonFormat(pattern = "dd.MM.yyyy HH:mm")
    private LocalDateTime time;
    private String content;

}
