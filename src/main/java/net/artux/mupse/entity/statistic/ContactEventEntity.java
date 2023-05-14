package net.artux.mupse.entity.statistic;

import lombok.Getter;
import lombok.Setter;
import net.artux.mupse.entity.BaseEntity;
import net.artux.mupse.entity.contact.ContactEntity;
import net.artux.mupse.entity.mailing.MailingEntity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Setter
@Getter
@Entity
@Table(name = "contact_event")
public class ContactEventEntity extends BaseEntity {

    @ManyToOne
    private ContactEntity contact;
    @ManyToOne
    private MailingEntity mailing;

    private LocalDateTime time;

    @Enumerated(EnumType.STRING)
    private EventType type;

}
