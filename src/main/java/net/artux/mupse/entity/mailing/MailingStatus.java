package net.artux.mupse.entity.mailing;

public enum MailingStatus {

    DRAFT("Черновик"),
    QUEUE("Запланировано"),
    RUNNING("Выполняется"),
    DONE("Выполнена");

    public String name;

    MailingStatus(String name) {
        this.name = name;
    }
}
