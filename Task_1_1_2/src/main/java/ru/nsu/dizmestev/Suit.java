package ru.nsu.dizmestev;

/**
 * Масть карты.
 */
public enum Suit {
    /** Червы */
    HEARTS("Червы"),
    /** Бубны */
    DIAMONDS("Бубны"),
    /** Трефы */
    CLUBS("Трефы"),
    /** Пики */
    SPADES("Пики");

    private final String name;

    /**
     * Записать название масти.
     * @param name масть
     */
    Suit(String name) {
        this.name = name;
    }

    /**
     * Получить масть.
     * @return масть
     */
    public String getName() {
        return name;
    }
}