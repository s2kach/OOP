package ru.nsu.dizmestev;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Рука игрока или дилера.
 *
 */
public class Hand {
    private static final int BLACKJACK_NUMBER = 21;
    private final List<Card> cards = new ArrayList<>();

    /**
     * Добавить карту в руку.
     *
     * @param card карта из колоды
     */
    public void addCard(Card card) {
        cards.add(card);
    }

    /**
     * Посчитать стоимость карт с учетом тузов либо 1 либо 11.
     *
     * @return счёт
     */
    public int calculateScore() {
        int score = 0;
        int aces = 0;

        for (Card card : cards) {
            score += card.getValue();
            if (card.getValue() == 11) {
                aces++;
            }
        }

        while (score > BLACKJACK_NUMBER && aces > 0) {
            score -= 10;
            aces--;
        }

        return score;
    }

    /**
     * Получить первую карту в руке.
     *
     * @return первая карта
     */
    public Card getFirstCard() {
        if (cards.isEmpty()) {
            throw new IllegalStateException("В руке нет карт");
        }
        return cards.get(0);
    }

    /**
     * Получить вторую карту в руке.
     *
     * @return вторая карта
     */
    public Card getSecondCard() {
        if (cards.size() < 2) {
            throw new IllegalStateException("В руке меньше двух карт");
        }
        return cards.get(1);
    }

    /**
     * Получить количество карт в руке.
     *
     * @return количество карт
     */
    public int getCardCount() {
        return cards.size();
    }

    /**
     * Получить все карты в виде неизменяемого списка (только для отладки и тестирования).
     *
     * @return неизменяемый список карт
     */
    public List<Card> getAllCards() {
        return Collections.unmodifiableList(cards);
    }

    /**
     * Это блекджек.
     *
     * @return набрали 21
     */
    public boolean hasBlackjack() {
        return cards.size() == 2 && calculateScore() == BLACKJACK_NUMBER;
    }

    /**
     * Текстовое представление карт с скрытием карты диллера при небходимости.
     *
     * @param hideSecondCard скрывать ли вторую карту
     * @return форматированная строка
     */
    public String toString(boolean hideSecondCard) {
        if (hideSecondCard && cards.size() >= 2) {
            return "[" + getFirstCard() + ", <закрытая карта>]";
        }
        return cards.toString();
    }

    /**
     * Очистить руку.
     */
    public void clear() {
        cards.clear();
    }
}