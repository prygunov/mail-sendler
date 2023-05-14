package net.artux.mupse.service.parameter;

import net.artux.mupse.entity.contact.ContactEntity;
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
