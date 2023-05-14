package net.artux.sendler.service.parameter;

import net.artux.sendler.entity.contact.ContactEntity;

public interface ParameterService {

    String putParameters(String content, ContactEntity contact);

}
