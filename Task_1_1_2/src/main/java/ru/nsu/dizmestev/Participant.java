package ru.nsu.dizmestev;

/**
 * Абстрактный класс участника игры.
 */
public abstract class Participant {
    /**
     * Рука.
     */
    protected final Hand hand = new Hand();

    /**
     * Получить руку с картами.
     *
     * @return объект руки
     */
    public Hand getHand() {
        return hand;
    }

    /**
     * Добавить карту в руку.
     *
     * @param card карта для добавления
     */
    public void takeCard(Card card) {
        hand.addCard(card);
    }

    /**
     * Будем ли добирать карту.
     *
     * @return true если берём
     */
    public abstract boolean shouldTakeCard();

    /**
     * Очистить руку.
     */
    public void clearHand() {
        hand.clear();
    }
}