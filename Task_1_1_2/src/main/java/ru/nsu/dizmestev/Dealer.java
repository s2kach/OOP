package ru.nsu.dizmestev;

/**
 * Дилер
 */
public class Dealer extends Participant {
    /**
     * Нужно ли добирать.
     * @return добираем если нет хотя бы 17
     */
    @Override
    public boolean shouldTakeCard() {
        return hand.calculateScore() < 17;
    }
}