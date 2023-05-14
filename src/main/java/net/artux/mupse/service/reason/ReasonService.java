package net.artux.mupse.service.reason;

import net.artux.mupse.model.page.QueryPage;
import net.artux.mupse.model.page.ResponsePage;
import net.artux.mupse.model.reason.ReasonDto;

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
