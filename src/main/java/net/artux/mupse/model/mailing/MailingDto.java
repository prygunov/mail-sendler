package net.artux.mupse.model.mailing;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import net.artux.mupse.entity.mailing.MailingStatus;
import net.artux.mupse.model.contact.ContactGroupDto;
import net.artux.mupse.model.settings.UserMailDto;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
public class MailingDto {

    private Long id;
    private UserMailDto mail;
    private String nameFrom;
    private String subject;
    private String content;
    private List<ContactGroupDto> groups;
    private MailingStatus status;
    @JsonFormat(pattern = "dd.MM.yyyy HH:mm")
    private LocalDateTime time;

}
