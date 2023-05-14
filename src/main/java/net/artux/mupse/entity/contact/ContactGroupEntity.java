package net.artux.mupse.entity.contact;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.artux.mupse.entity.BaseEntity;
import net.artux.mupse.entity.mailing.MailingEntity;
import net.artux.mupse.entity.user.UserEntity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "contact_group")
public class ContactGroupEntity extends BaseEntity {

    @ManyToOne
    private UserEntity owner;
    private String name;
    private String description;
    private String hexColor;

    @ManyToMany
    @JoinTable(name = "contact_group_contacts",
            joinColumns = @JoinColumn(name = "groups_id"),
            inverseJoinColumns = @JoinColumn(name = "contacts_id"))
    private Set<ContactEntity> contacts = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "user_mailing_groups",
            joinColumns = @JoinColumn(name = "groups_id"),
            inverseJoinColumns = @JoinColumn(name = "mailing_entity_id"))
    private Set<MailingEntity> mailings = new HashSet<>();

    public ContactGroupEntity(UserEntity owner, String name) {
        this.owner = owner;
        this.name = name;
    }
}
