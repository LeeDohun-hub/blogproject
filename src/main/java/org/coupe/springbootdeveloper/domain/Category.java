package org.coupe.springbootdeveloper.domain;

public enum Category {
    GAME("게임"),
    DEV("개발"),
    SPORTS("운동"),
    BOOK("독서"),
    ETC("기타");

    private final String label;

    Category(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
