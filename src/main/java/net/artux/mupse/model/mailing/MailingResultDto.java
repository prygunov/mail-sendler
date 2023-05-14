package net.artux.mupse.model.mailing;

import lombok.Data;

@Data
public class MailingResultDto {

    private int contacts;
    private int success;
    private int attempts;
    private long openings;
    private long linkOpenings;
    private long events;

}
