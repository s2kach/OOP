package ru.nsu.dizmestev;

import java.util.List;

/**
 * Провайдер фиксированной колоды для тестирования.
 */
public class FixedDeckProvider implements DeckProvider {
    private final List<Card> cards;

    /**
     * Конструктор получающй карты.
     *
     * @param cards список карт
     */
    public FixedDeckProvider(List<Card> cards) {
        this.cards = cards;
    }

    @Override
    public Deck getDeck() {
        Deck deck = new Deck(0);
        for (int i = cards.size() - 1; i >= 0; i--) {
            deck.addCard(cards.get(i));
        }
        return deck;
    }
}