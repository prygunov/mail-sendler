package net.artux.mupse.model.file;

import lombok.Data;

@Data
public class AttachmentDto {

    private String name;
    private String url;
    private String type;
    private long size;
}
