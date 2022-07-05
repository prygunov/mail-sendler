package net.artux.mupse.entity.contact;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.artux.mupse.entity.BaseEntity;
import net.artux.mupse.entity.user.UserEntity;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Setter
@Getter
@Entity
@RequiredArgsConstructor
@Table(name = "contact")
public class ContactEntity extends BaseEntity {

    @ManyToOne
    private UserEntity owner;

    private String name;
    private String email;

    @Column(unique = true)
    private UUID token;

    private boolean disabled;

    @ManyToMany
    @JoinTable(name = "contact_group_contacts",
            joinColumns = @JoinColumn(name = "contacts_id"),
            inverseJoinColumns = @JoinColumn(name = "groups_id"))
    private List<ContactGroupEntity> groups;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ContactEntity entity = (ContactEntity) o;

        return Objects.equals(id, entity.id);
    }

    public ContactEntity(UserEntity owner, String name) {
        this.owner = owner;
        this.name = name;
    }

    public ContactEntity(UserEntity owner, String name, List<ContactGroupEntity> groups) {
        this.owner = owner;
        this.name = name;
        this.groups = groups;
    }
}
