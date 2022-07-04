package net.artux.mupse.entity.contact;

import com.opencsv.bean.CsvBindByPosition;
import lombok.Getter;
import lombok.Setter;
import net.artux.mupse.entity.BaseEntity;
import net.artux.mupse.entity.user.UserEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

@Setter
@Getter
@Entity
@Table(name = "temp_contact")
public class TempContactEntity extends BaseEntity {

    @CsvBindByPosition(position = 1)
    private String name;

    @CsvBindByPosition(position = 0)
    @NotEmpty
    private String email;

    @ManyToOne
    private UserEntity owner;

}
