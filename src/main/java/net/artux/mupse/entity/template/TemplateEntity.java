package net.artux.mupse.entity.template;

import lombok.Getter;
import lombok.Setter;
import net.artux.mupse.entity.BaseEntity;
import net.artux.mupse.entity.user.UserEntity;
import org.springframework.stereotype.Service;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "mail_template")
public class TemplateEntity extends BaseEntity {

    @ManyToOne
    private UserEntity owner;

    private String subject;
    private String content;

}
