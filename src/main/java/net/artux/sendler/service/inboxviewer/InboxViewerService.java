package net.artux.sendler.service.inboxviewer;

import net.artux.sendler.entity.settings.UserMailEntity;

import java.util.List;

public interface InboxViewerService {
    List<String> checkInvalidAddress(UserMailEntity userMailEntity);

    int[] getConfigs(UserMailEntity userMailEntity);
}
