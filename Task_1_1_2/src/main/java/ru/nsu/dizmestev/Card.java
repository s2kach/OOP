package ru.nsu.dizmestev;

/**
 * Сама карта.
 * @param suit Масть
 * @param rank Значение
 */
public record Card(Suit suit, Rank rank) {

    /**
     * Получить зачение карты.
     * @return значение карты
     */
    public int getValue() {
        return rank.getValue();
    }

    @Override
    public String toString() {
        return rank.getName() + " " + suit.getName() + " (" + getValue() + ")";
    }
}