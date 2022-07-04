package net.artux.mupse.entity.mailing;

import net.artux.mupse.entity.BaseEntity;
import net.artux.mupse.entity.contact.ContactGroupEntity;
import net.artux.mupse.entity.template.TemplateEntity;
import net.artux.mupse.entity.user.UserEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "mailing")
public class MailingEntity extends BaseEntity {

    @ManyToOne
    private UserEntity owner;

    @OneToOne
    private TemplateEntity template;

    @ManyToOne
    private ContactGroupEntity group;

    private MailingType type;
    private Date time;

}
