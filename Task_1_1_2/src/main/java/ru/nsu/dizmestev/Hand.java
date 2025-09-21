package ru.nsu.dizmestev;

import java.util.ArrayList;
import java.util.List;

/**
 * Рука игрока или дилера.
 */
public class Hand {
    private final List<Card> cards = new ArrayList<>();

    /**
     * Добавить карту в руку.
     * @param card карта из колоды
     */
    public void addCard(Card card) {
        cards.add(card);
    }

    /**
     * Посчитать стоимость карт с учетом тузов либо 1 либо 11.
     * @return счёт
     */
    public int calculateScore() {
        int score = 0;
        int aces = 0;

        for (Card card : cards) {
            score += card.getValue();
            if (card.getValue() == 11) aces++;
        }

        while (score > 21 && aces > 0) {
            score -= 10;
            aces--;
        }

        return score;
    }

    /**
     * Получить список всех карт.
     * @return список
     */
    public List<Card> getCards() {
        return new ArrayList<>(cards);
    }

    /**
     * Это блекджек.
     * @return набрали 21
     */
    public boolean hasBlackjack() {
        return calculateScore() == 21;
    }

    /**
     * Текстовое значение карт.
     * @return строка
     */
    @Override
    public String toString() {
        return cards.toString();
    }

    /**
     * Очистить руку.
     */
    public void clear() {
        cards.clear();
    }
}