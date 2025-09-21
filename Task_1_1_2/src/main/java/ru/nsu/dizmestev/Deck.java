package ru.nsu.dizmestev;

import java.util.Collections;
import java.util.Stack;

/**
 * Колода.
 */
public class Deck {
    /**
     * Стек карт.
     */
    private final Stack<Card> cards = new Stack<>();

    /**
     * Создание колоды и перемешивание.
     * @param numberOfDecks сколько колод будет
     */
    public Deck(int numberOfDecks) {
        for (int i = 0; i < numberOfDecks; i++) {
            for (Suit suit : Suit.values()) {
                for (Rank rank : Rank.values()) {
                    cards.push(new Card(suit, rank));
                }
            }
        }
        shuffle();
    }

    /**
     * Перемешать используя метод из collections.
     */
    public void shuffle() {
        Collections.shuffle(cards);
    }

    /**
     * Достать одну карту.
     * @return достаём из стека карту если она есть
     */
    public Card drawCard() {
        if (cards.isEmpty()) {
            throw new IllegalStateException("Колода пуста");
        }
        return cards.pop();
    }

    /**
     * Размер колоды (для тестов).
     * @return вернули размер
     */
    public int size() {
        return cards.size();
    }
}