package net.artux.sendler.entity.reason;

import lombok.Getter;
import lombok.Setter;
import net.artux.sendler.entity.BaseEntity;
import net.artux.sendler.entity.contact.ContactEntity;

import javax.persistence.*;
import java.time.LocalDateTime;

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
