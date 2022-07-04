package net.artux.mupse.entity.mailing;

public enum MailingType {

    DRAFT("Черновик"),
    SCHEDULED("Отложена"),
    DONE("Выполнена");

    public String name;

    MailingType(String name) {
        this.name = name;
    }
}
