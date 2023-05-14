package net.artux.mupse.model.user;

import lombok.Data;

import java.util.UUID;

@Data
public class UserDto {

    private String name;
    private String login;
    private String email;
    private UUID token;

}
