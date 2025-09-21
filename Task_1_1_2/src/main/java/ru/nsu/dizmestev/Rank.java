package ru.nsu.dizmestev;

/**
 * Значение карты.
 */
public enum Rank {
    /** Двойка. */
    TWO(2, "Двойка"),
    /** Тройка. */
    THREE(3, "Тройка"),
    /** Четверка. */
    FOUR(4, "Четверка"),
    /** Пятерка. */
    FIVE(5, "Пятерка"),
    /** Шестерка. */
    SIX(6, "Шестерка"),
    /** Семерка. */
    SEVEN(7, "Семерка"),
    /** Восьмерка. */
    EIGHT(8, "Восьмерка"),
    /** Девятка. */
    NINE(9, "Девятка"),
    /** Десятка. */
    TEN(10, "Десятка"),
    /** Валет. */
    JACK(10, "Валет"),
    /** Дама. */
    QUEEN(10, "Дама"),
    /** Король. */
    KING(10, "Король"),
    /** Туз. */
    ACE(11, "Туз");

    private final int value;
    private final String name;

    /**
     * Записать значение.
     *
     * @param value значение
     * @param name название
     */
    Rank(int value, String name) {
        this.value = value;
        this.name = name;
    }

    /**
     * Получить значение.
     *
     * @return значение
     */
    public int getValue() {
        return value;
    }

    /**
     * Получить имя.
     *
     * @return имя
     */
    public String getName() {
        return name;
    }
}