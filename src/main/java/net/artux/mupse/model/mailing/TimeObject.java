package net.artux.mupse.model.mailing;

import lombok.Data;

import javax.validation.constraints.Future;
import java.time.LocalDateTime;

@Data
public class TimeObject {

    @Future
    private LocalDateTime time;

}
