package net.artux.sendler.model.settings;

import lombok.Data;

@Data
public class UserMailDto {

    private Long id;
    private String host;
    private int port;

    private String username;

}