package net.artux.sendler.model.template;

import lombok.Data;

@Data
public class TemplateDto {

    private long id;
    private String title;
    private String subject;
    private String content;

}
