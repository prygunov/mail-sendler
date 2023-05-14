package net.artux.sendler.model.contact.creation;

import lombok.Builder;
import lombok.Data;
import net.artux.sendler.entity.contact.ContactEntity;

import java.util.List;

@Data
@Builder
public class ContactCreationResult {

    private List<ContactEntity> originalContacts;
    private List<ContactEntity> collisionContacts;

    private int all;
    private int regexRejected;
    private int duplicateRejected;

}
