package net.artux.sendler.model.contact;

import lombok.Data;

@Data
public class ParsingResult {

    private String filename;
    private int all;
    private int accepted;
    private int rejected;
    private int regexRejected;
    private int collisionRejected;


    public ParsingResult(String filename, int all, int accepted, int rejected, int regexRejected, int collisionRejected) {
        this.filename = filename;
        this.all = all;
        this.accepted = accepted;
        this.rejected = rejected;
        this.regexRejected = regexRejected;
        this.collisionRejected = collisionRejected;
    }
}
