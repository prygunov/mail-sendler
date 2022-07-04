package net.artux.mupse.entity.contact;

import lombok.Getter;
import lombok.Setter;
import net.artux.mupse.entity.BaseEntity;
import net.artux.mupse.entity.user.UserEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "contact_group")
public class ContactGroupEntity extends BaseEntity {

    @ManyToOne
    private UserEntity owner;
    private String name;
    private String description;

    @ManyToMany(mappedBy = "groups")
    private List<ContactEntity> contacts;

}
