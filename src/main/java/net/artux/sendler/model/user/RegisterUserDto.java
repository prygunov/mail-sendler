package net.artux.sendler.model.user;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class RegisterUserDto {

    @NotNull
    @NotBlank(message = "{user.name.notEmpty}")
    @Length(min = 4, max = 25, message = "{user.name.lengthError}")
    private String name;

    @NotBlank(message = "{user.login.notEmpty}")
    @Length(min = 4, max = 25, message = "{user.login.lengthError}")
    @Pattern(regexp = "^[a-zA-Z0-9-_.]+$", message = "{user.login.patternError}")
    private String login;

    @NotNull
    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", message = "{email.patternError}")
    private String email;

    @NotNull
    @NotBlank
    @Length(min = 8, max = 25, message = "{user.password.lengthError}")
    private String password;

}
