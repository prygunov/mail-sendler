package net.artux.sendler.service.sender;

import net.artux.sendler.entity.settings.UserMailEntity;

public interface SelfSenderService {
    boolean sendMailOnUserWorkAddress(UserMailEntity userMailEntity, String subject, String text);
}
