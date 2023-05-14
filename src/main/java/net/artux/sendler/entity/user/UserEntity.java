package net.artux.sendler.entity.user;


import lombok.Getter;
import lombok.Setter;
import net.artux.sendler.entity.BaseEntity;
import net.artux.sendler.entity.contact.ContactGroupEntity;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "app_user")
public class UserEntity extends BaseEntity {

    private String name;
    private String login;
    private String email;
    @Column(unique = true)
    private UUID token;
    private String password;
    @Enumerated(EnumType.STRING)
    private RoleType role;

    @OneToMany(fetch = FetchType.LAZY)
    private List<ContactGroupEntity> groups;

}
