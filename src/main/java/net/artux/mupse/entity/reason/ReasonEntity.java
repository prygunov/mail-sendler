package net.artux.mupse.entity.reason;

import lombok.Getter;
import lombok.Setter;
import net.artux.mupse.entity.BaseEntity;
import net.artux.mupse.entity.contact.ContactEntity;
import net.artux.mupse.entity.contact.ContactGroupEntity;
import net.artux.mupse.entity.mailing.MailingStatus;
import net.artux.mupse.entity.settings.UserMailEntity;
import net.artux.mupse.entity.user.UserEntity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "contact_reason")
public class ReasonEntity extends BaseEntity {

    @OneToOne
    private ContactEntity contact;
    private String content;
    private LocalDateTime time;

}
