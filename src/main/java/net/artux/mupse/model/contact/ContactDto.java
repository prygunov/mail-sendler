package net.artux.mupse.model.contact;

import lombok.Data;

import java.util.List;

@Data
public class ContactDto {

    private Long id;
    private String name;
    private String email;
    private String token;
    private boolean disabled;
    private List<ContactGroupDto> groups;

}
