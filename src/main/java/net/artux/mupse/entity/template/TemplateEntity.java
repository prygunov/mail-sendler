package net.artux.mupse.entity.template;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.artux.mupse.entity.BaseEntity;
import net.artux.mupse.entity.user.UserEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "mail_template")
public class TemplateEntity extends BaseEntity {

    @ManyToOne
    private UserEntity owner;

    private String subject;
    private String content;

}
