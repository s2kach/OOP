package ru.nsu.dizmestev;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Отвечает за размещение объектов на игровом поле.
 */
public class FieldSpawner {

    private final int width;
    private final int height;
    private final Random random;

    /**
     * Конструктор задающий ширину и высоту поля.
     *
     * @param width Ширина.
     * @param height Высота.
     */
    public FieldSpawner(int width, int height) {
        this.width = width;
        this.height = height;
        this.random = new Random();
    }

    /**
     * Ищет свободную точку на поле, не занятую змейкой, препятствиями или едой.
     *
     * @param snake Змейка.
     * @param obstacles Препятствия.
     * @param foods Еда.
     * @return Свободная точка.
     */
    public Point findEmptyPoint(Snake snake, List<Point> obstacles, List<Food> foods) {
        while (true) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            Point p = new Point(x, y);

            if (!snake.getBody().contains(p) && !obstacles.contains(p)
                    && !foods.stream().anyMatch(f -> f.getPosition().equals(p))) {
                return p;
            }
        }
    }

    /**
     * Генерирует список начальных препятствий, избегая линии появления змейки.
     *
     * @param count Сколько нужно.
     * @param snake Змейка.
     * @return Список точек.
     */
    public List<Point> generateObstacles(int count, Snake snake) {
        List<Point> obstacles = new ArrayList<>();
        Point head = snake.getHead();
        int startY = head.getY();
        int startX = head.getX();
        while (obstacles.size() < count) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            Point p = new Point(x, y);

            boolean isOnSnakeLine = (y == startY && x >= startX);

            if (!snake.getBody().contains(p) && !obstacles.contains(p) && !isOnSnakeLine) {
                obstacles.add(p);
            }
        }
        return obstacles;
    }

    /**
     * Создает новую еду на свободной клетке.
     *
     * @param snake Змейка.
     * @param obstacles Препятствия.
     * @param foods Другая еда.
     * @return Новая еда.
     */
    public Food spawnFood(Snake snake, List<Point> obstacles, List<Food> foods) {
        Point p = findEmptyPoint(snake, obstacles, foods);
        return new SimpleFood(p);
    }
}