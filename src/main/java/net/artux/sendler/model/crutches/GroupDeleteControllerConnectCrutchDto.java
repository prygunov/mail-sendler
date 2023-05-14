package net.artux.sendler.model.crutches;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class GroupDeleteControllerConnectCrutchDto {

    @NotNull
    private Long id;

    @NotNull
    private boolean deleteContacts;
}
