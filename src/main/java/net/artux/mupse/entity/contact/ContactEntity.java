package net.artux.mupse.entity.contact;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.artux.mupse.entity.BaseEntity;
import net.artux.mupse.entity.user.UserEntity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.List;

@Setter
@Getter
@Entity
@NoArgsConstructor
@Table(name = "contact")
public class ContactEntity extends BaseEntity {

    @ManyToOne
    private UserEntity owner;

    private String name;

    private String email;
    private boolean disabled;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<ContactGroupEntity> groups;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ContactEntity entity = (ContactEntity) o;

        return id == entity.id;
    }

    public ContactEntity(UserEntity owner, String name) {
        this.owner = owner;
        this.name = name;
    }
}
