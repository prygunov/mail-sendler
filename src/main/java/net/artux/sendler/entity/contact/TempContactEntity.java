package net.artux.sendler.entity.contact;

import lombok.Getter;
import lombok.Setter;
import net.artux.sendler.entity.BaseEntity;
import net.artux.sendler.entity.user.UserEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Setter
@Getter
@Entity
@Table(name = "temp_contact")
public class TempContactEntity extends BaseEntity {

    private String name;

    private String email;

    @ManyToOne
    private UserEntity owner;

}
