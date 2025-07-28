package org.coupe.springbootdeveloper.domain;

public enum Category {
    GAME("게임"),
    PROGRAMMING("개발"),
    SPORTS("운동"),
    READING("독서"),
    MEMO("메모");

    private final String label;

    Category(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
