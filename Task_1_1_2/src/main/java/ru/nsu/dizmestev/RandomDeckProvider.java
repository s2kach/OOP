package ru.nsu.dizmestev;

/**
 * Провайдер случайной колоды.
 */
public class RandomDeckProvider implements DeckProvider {
    private final int numberOfDecks;

    /**
     * Конструктор получающий количество колод.
     *
     * @param numberOfDecks число
     */
    public RandomDeckProvider(int numberOfDecks) {
        this.numberOfDecks = numberOfDecks;
    }

    @Override
    public Deck getDeck() {
        return new Deck(numberOfDecks);
    }
}