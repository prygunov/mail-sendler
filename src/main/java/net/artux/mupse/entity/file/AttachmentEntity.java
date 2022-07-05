package net.artux.mupse.entity.file;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.artux.mupse.entity.BaseEntity;
import net.artux.mupse.entity.user.UserEntity;

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
