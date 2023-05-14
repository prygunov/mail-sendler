package net.artux.sendler.entity.file;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.artux.sendler.entity.BaseEntity;
import net.artux.sendler.entity.user.UserEntity;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "attachment")
public class AttachmentEntity extends BaseEntity {

    @ManyToOne
    private UserEntity owner;

    private String name;
    private String type;
    @Lob
    private byte[] data;

    public AttachmentEntity(String name, String type, byte[] data) {
        this.name = name;
        this.type = type;
        this.data = data;
    }
}
