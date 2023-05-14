package net.artux.sendler.model.contact.creation;

import lombok.Data;

@Data
public class ContactCreationResultDto {

    private int all;
    private int accepted;
    private int regexRejected;
    private int duplicateRejected;
    private int collisionRejected;

}
