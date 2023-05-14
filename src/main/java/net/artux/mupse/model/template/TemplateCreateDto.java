package net.artux.mupse.model.template;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class TemplateCreateDto {

    @NotBlank
    private String title;
    @NotBlank
    private String subject;
    @NotBlank
    private String content;

}
