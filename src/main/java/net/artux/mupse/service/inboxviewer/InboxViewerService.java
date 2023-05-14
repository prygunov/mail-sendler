package net.artux.mupse.service.inboxviewer;

import net.artux.mupse.entity.settings.UserMailEntity;

import java.util.List;

public interface InboxViewerService {
    List<String> checkInvalidAddress(UserMailEntity userMailEntity);

    int[] getConfigs(UserMailEntity userMailEntity);
}
