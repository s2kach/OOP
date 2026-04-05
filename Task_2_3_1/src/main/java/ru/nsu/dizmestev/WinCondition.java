package ru.nsu.dizmestev;

/**
 * Интерфейс условия победы.
 */
public interface WinCondition {

    /**
     * Функция проверки события победы.
     *
     * @param snake Змейка, которую проверяем.
     * @return true если победа.
     */
    boolean isWon(Snake snake);
}
