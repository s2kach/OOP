package ru.nsu.dizmestev;

import java.util.Objects;

/**
 * Класс, представляющий координаты на игровом поле.
 */
public class Point {

    private final int x;
    private final int y;

    /**
     * Конструктор для создания точки.
     *
     * @param x Координата по оси X.
     * @param y Координата по оси Y.
     */
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Получает координату X.
     *
     * @return Значение X.
     */
    public int getX() {
        return x;
    }

    /**
     * Получает координату Y.
     *
     * @return Значение Y.
     */
    public int getY() {
        return y;
    }

    /**
     * Сравнивает точки на совпадение координат.
     *
     * @param o Объект для сравнения.
     * @return Результат сравнения.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return x == point.x && y == point.y;
    }

    /**
     * Вычисляет хэш-код точки.
     *
     * @return Хэш-код.
     */
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
