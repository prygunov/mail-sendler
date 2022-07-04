package net.artux.mupse.model.contact;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import net.artux.mupse.entity.contact.ContactEntity;

import java.util.List;

@Data
@RequiredArgsConstructor
public class ContactContainer {

    private int all;
    private int regexRejected;

    private List<ContactEntity> originalContacts;
    private List<ContactEntity> collisionContacts;

    public ContactContainer(List<ContactEntity> originalContacts, List<ContactEntity> collisionContacts, int all) {
        this.originalContacts = originalContacts;
        this.collisionContacts = collisionContacts;

        this.all = all;
        this.regexRejected = all - originalContacts.size() - collisionContacts.size();
    }
}
