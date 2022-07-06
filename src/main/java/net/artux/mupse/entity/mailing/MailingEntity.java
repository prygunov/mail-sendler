package net.artux.mupse.entity.mailing;

import net.artux.mupse.entity.BaseEntity;
import net.artux.mupse.entity.contact.ContactGroupEntity;
import net.artux.mupse.entity.template.TemplateEntity;
import net.artux.mupse.entity.user.UserEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "mailing")
public class MailingEntity extends BaseEntity {

    @ManyToOne
    private UserEntity owner;

    @OneToOne
    private TemplateEntity template;

    @ManyToMany
    private List<ContactGroupEntity> group;

    private MailingType type;
    private Date time;

}
