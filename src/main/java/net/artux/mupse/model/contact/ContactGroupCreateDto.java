package net.artux.mupse.model.contact;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class ContactGroupCreateDto {

    @NotEmpty
    private String name;
    private String description;

}
