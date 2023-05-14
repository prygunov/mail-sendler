package net.artux.mupse.model.settings;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class UserMailCreateDto {

    @NotBlank
    private String host;
    @Min(0)
    @Max(65535)
    private int port;

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", message = "{email.patternError}")
    private String username;
    @NotBlank
    private String password;

}
