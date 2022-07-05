package net.artux.mupse.model.user;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.UUID;

@Data
public class UserDto {

    @NotNull
    @NotEmpty(message = "{user.name.notEmpty}")
    @Length(min = 4, max = 25, message = "{user.name.lengthError}")
    private String name;

    @NotEmpty(message = "{user.login.notEmpty}")
    @Length(min = 4, max = 25, message = "{user.login.lengthError}")
    @Pattern(regexp = "^[a-zA-Z0-9-_.]+$", message = "{user.login.patternError}")
    private String login;

    @NotNull
    @NotEmpty
    @Pattern(regexp = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", message = "{user.email.patternError}")
    private String email;

    private UUID token;

}
