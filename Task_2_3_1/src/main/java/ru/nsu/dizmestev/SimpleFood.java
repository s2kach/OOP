package ru.nsu.dizmestev;

/**
 * Обычная еда которая просто увеличивает размер на 1.
 */
public class SimpleFood implements Food {
    private final Point position;

    /**
     * Конструктор устанавливающий точку данной еды.
     *
     * @param position Точка еды.
     */
    public SimpleFood(Point position) {
        this.position = position;
    }

    @Override
    public Point getPosition() {
        return position;
    }

    @Override
    public void applyEffect(Snake snake) {
        snake.grow();
    }
}
