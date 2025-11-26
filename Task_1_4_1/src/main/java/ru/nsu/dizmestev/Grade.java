package ru.nsu.dizmestev;

/**
 * Перечисление возможных оценок студента.
 */
public enum Grade {
    EXCELLENT(5),
    GOOD(4),
    SATISFACTORY(3);

    private final int value;

    /**
     * Создает оценку с числовым значением.
     */
    Grade(int value) {
        this.value = value;
    }

    /**
     * Возвращает числовое представление оценки.
     *
     * @return Числовое значение оценки.
     */
    public int getValue() {
        return value;
    }
}