package net.artux.sendler.entity.settings;

import lombok.Getter;
import lombok.Setter;
import net.artux.sendler.entity.BaseEntity;
import net.artux.sendler.entity.user.UserEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "user_mail")
public class UserMailEntity extends BaseEntity {

    @ManyToOne
    private UserEntity owner;

    private String host;
    private int port;

    private String username;
    private String password;

}
