package net.artux.sendler.entity.statistic;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.artux.sendler.entity.BaseEntity;
import net.artux.sendler.entity.contact.ContactEntity;
import net.artux.sendler.entity.mailing.MailingEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "mailing_event")
@NoArgsConstructor
public class MailingEventEntity extends BaseEntity {

    @ManyToOne
    private ContactEntity contact;
    @ManyToOne
    private MailingEntity mailing;
    private String content;
    private Date time;

    public MailingEventEntity(MailingEntity mailing, String content) {
        this.mailing = mailing;
        this.content = content;
    }
}
