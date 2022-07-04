package net.artux.mupse.model.contact;

import lombok.Data;

@Data
public class ContactGroupDto {

    private long id;
    private String name;
    private String description;
    private int contacts;

}
