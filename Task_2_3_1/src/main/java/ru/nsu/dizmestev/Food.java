package ru.nsu.dizmestev;

/**
 * Интерфейс для различных типов еды.
 */
public interface Food {
    /**
     * Получить координаты точки еды.
     *
     * @return Координаты еды.
     */
    Point getPosition();

    /**
     * Применяет эффект еды к змейке.
     *
     * @param snake Объект змейки.
     */
    void applyEffect(Snake snake);
}
