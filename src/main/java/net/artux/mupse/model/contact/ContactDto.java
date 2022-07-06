package net.artux.mupse.model.contact;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

@Data
public class ContactDto {

    private Long id;
    private String name;
    private String email;
    private boolean disabled;
    private List<ContactGroupDto> groups;

}
