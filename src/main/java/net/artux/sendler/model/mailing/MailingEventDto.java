package net.artux.sendler.model.mailing;

import lombok.Data;

import java.util.Date;

@Data
public class MailingEventDto {

    private String email;
    private Date time;
    private String content;

}
