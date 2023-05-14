package net.artux.sendler.entity.statistic;

import lombok.Getter;
import lombok.Setter;
import net.artux.sendler.entity.BaseEntity;
import net.artux.sendler.entity.contact.ContactEntity;
import net.artux.sendler.entity.mailing.MailingEntity;

import javax.persistence.*;
import java.time.LocalDateTime;

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
