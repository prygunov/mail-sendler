package net.artux.mupse.model.contact;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Data
public class ContactGroupCreateDto {

    @NotBlank
    private String name;
    private String description;
    @Pattern(regexp = "^#([a-fA-F0-9]{6}|[a-fA-F0-9]{3})$")
    private String hexColor;

}
