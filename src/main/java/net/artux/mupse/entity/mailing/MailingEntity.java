package net.artux.mupse.entity.mailing;

import lombok.Getter;
import lombok.Setter;
import net.artux.mupse.entity.BaseEntity;
import net.artux.mupse.entity.contact.ContactGroupEntity;
import net.artux.mupse.entity.settings.UserMailEntity;
import net.artux.mupse.entity.user.UserEntity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
@Entity
@Table(name = "user_mailing")
public class MailingEntity extends BaseEntity {

    @ManyToOne
    private UserEntity owner;

    @ManyToOne
    private UserMailEntity mail;

    private String nameFrom;
    private String subject;
    @Lob
    private String content;
    private UUID token;

    @ManyToMany
    @JoinTable(name = "user_mailing_groups",
            joinColumns = @JoinColumn(name = "mailing_entity_id"),
            inverseJoinColumns = @JoinColumn(name = "groups_id"))
    private Set<ContactGroupEntity> groups = new HashSet<>();

    @Enumerated(EnumType.STRING)
    private MailingStatus status;
    private LocalDateTime time;

}
