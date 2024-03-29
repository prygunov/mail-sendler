package net.artux.sendler.model.reason;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ReasonModel {

    @NotBlank
    private String reason;
    private String content;

}
