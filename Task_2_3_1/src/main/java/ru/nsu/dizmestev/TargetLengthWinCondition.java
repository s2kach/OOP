package ru.nsu.dizmestev;

/**
 * Реализация интерфейса условия победы - по достижению длины.
 */
public class TargetLengthWinCondition implements WinCondition {
    private final int targetLength;

    /**
     * Конструктор устанавливающий значение целевой длины.
     *
     * @param targetLength Целевая длина змейки.
     */
    public TargetLengthWinCondition(int targetLength) {
        this.targetLength = targetLength;
    }

    @Override
    public boolean isWon(Snake snake) {
        return snake.getBody().size() >= targetLength;
    }
}
