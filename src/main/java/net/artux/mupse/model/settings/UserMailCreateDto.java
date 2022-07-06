package net.artux.mupse.model.settings;

import lombok.Data;

import javax.validation.constraints.*;

@Data
public class UserMailCreateDto {

    @NotBlank
    private String protocol = "smtp";
    @NotBlank
    private String host;
    @Min(0)
    @Max(65535)
    private int port;

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    private String username;
    @NotBlank
    private String password;

}
