package net.artux.mupse.entity.user;


import lombok.Getter;
import lombok.Setter;
import net.artux.mupse.entity.BaseEntity;
import net.artux.mupse.entity.contact.ContactGroupEntity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "app_user")
public class UserEntity extends BaseEntity {

    private String name;
    private String login;
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private RoleType role;

    @OneToMany(fetch = FetchType.LAZY)
    private List<ContactGroupEntity> groups;

}
