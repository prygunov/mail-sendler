package net.artux.sendler.service.reason;

import net.artux.sendler.model.page.QueryPage;
import net.artux.sendler.model.page.ResponsePage;
import net.artux.sendler.model.reason.ReasonDto;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.UUID;

public interface ReasonService {


    ResponsePage<ReasonDto> getReasons(QueryPage queryPage);

    ByteArrayInputStream exportAll() throws IOException;

    void clear(boolean clearContacts);

    void createReason(UUID uuid);

    void setReasonContent(UUID uuid, String content);
}
