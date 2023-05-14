package net.artux.mupse.entity.statistic;

import lombok.Getter;
import lombok.Setter;
import net.artux.mupse.entity.BaseEntity;
import net.artux.mupse.entity.mailing.MailingEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "mailing_result")
public class MailingResultEntity extends BaseEntity {

    @ManyToOne
    private MailingEntity mailing;

    private int contacts;
    private int attempts;
    private int success;

}
