package net.artux.sendler.service.parameter;

import net.artux.sendler.entity.contact.ContactEntity;
import org.springframework.stereotype.Service;

@Service
public class ParameterServiceImpl implements ParameterService {

    @Override
    public String putParameters(String content, ContactEntity contact) {
        return content
                .replaceAll("\\{\\{name}}", contact.getName())
                .replaceAll("\\{\\{email}}", contact.getEmail())
                .replaceAll("\\{\\{token}}", contact.getToken().toString());
    }
}
