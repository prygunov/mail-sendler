package net.artux.sendler.model.contact;

import lombok.Data;

@Data
public class ContactGroupDto {

    private long id;
    private String name;
    private String description;
    private String hexColor;
    private int contacts;

}
