package ru.nsu.dizmestev;

import java.util.Objects;

/**
 * Класс, представляющий координаты на игровом поле.
 */
public class Point {
    private final int posX;
    private final int posY;

    /**
     * Конструктор для создания точки.
     *
     * @param posX Координата по оси X.
     * @param posY Координата по оси Y.
     */
    public Point(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
    }

    /**
     * Получает координату X.
     *
     * @return Значение X.
     */
    public int getX() {
        return posX;
    }

    /**
     * Получает координату Y.
     *
     * @return Значение Y.
     */
    public int getY() {
        return posY;
    }

    /**
     * Сравнивает точки на совпадение координат.
     *
     * @param o Объект для сравнения.
     * @return Результат сравнения.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Point point = (Point) o;
        return posX == point.posX && posY == point.posY;
    }

    /**
     * Вычисляет хэш-код точки.
     *
     * @return Хэш-код.
     */
    @Override
    public int hashCode() {
        return Objects.hash(posX, posY);
    }
}
