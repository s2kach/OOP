package ru.nsu.dizmestev;

/**
 * Перечисление возможных оценок студента.
 */
public enum Grade {
    /**
     * Отлично.
     */
    EXCELLENT(5),
    /**
     * Харашо.
     */
    GOOD(4),
    /**
     * Удовлетворительно.
     */
    SATISFACTORY(3);

    private final int value;

    /**
     * Создает оценку с числовым значением.
     *
     * @param value Оценка.
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