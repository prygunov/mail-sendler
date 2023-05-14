package net.artux.mupse.model.mailing;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class MailingCreateDto {

    @NotNull
    private Long mailId;
    @NotBlank
    private String nameFrom;
    @NotBlank
    private String subject;
    @NotBlank
    private String content;

    @NotEmpty
    private List<Long> groupIds;

}
