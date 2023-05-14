package net.artux.mupse.service.sender;

import net.artux.mupse.entity.settings.UserMailEntity;

public interface SelfSenderService {
    boolean sendMailOnUserWorkAddress(UserMailEntity userMailEntity, String subject, String text);
}
