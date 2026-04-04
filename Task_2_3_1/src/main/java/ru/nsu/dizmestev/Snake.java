package ru.nsu.dizmestev;

import java.util.LinkedList;

/**
 * Класс, представляющий змейку в игре.
 */
public class Snake {

    private final LinkedList<Point> body;
    private Direction currentDirection;
    private boolean growNextMove;
    private boolean directionChangedInThisTick;

    /**
     * Инициализирует змейку из одного звена.
     *
     * @param startX Начальная позиция по X.
     * @param startY Начальная позиция по Y.
     */
    public Snake(int startX, int startY) {
        this.body = new LinkedList<>();
        this.body.add(new Point(startX, startY));
        this.body.add(new Point(startX - 1, startY));
        this.body.add(new Point(startX - 2, startY));
        this.currentDirection = Direction.RIGHT;
        this.growNextMove = false;
        this.directionChangedInThisTick = false;
    }

    /**
     * Двигает змейку.
     */
    public void move() {
        body.addFirst(calculateNextHead());

        if (growNextMove) {
            growNextMove = false;
        } else {
            body.removeLast();
        }

        directionChangedInThisTick = false;
    }

    /**
     * Вычисляет, где окажется голова на следующем шаге без изменения состояния змейки.
     */
    public Point calculateNextHead() {
        Point head = body.getFirst();
        int newX = head.getX();
        int newY = head.getY();

        switch (currentDirection) {
            case UP -> newY--;
            case DOWN -> newY++;
            case LEFT -> newX--;
            case RIGHT -> newX++;
            default -> {}
        }
        return new Point(newX, newY);
    }

    /**
     * Задает новое направление движения с защитой от двойного поворота за тик.
     *
     * @param newDirection Новое направление.
     */
    public void setDirection(Direction newDirection) {
        if (directionChangedInThisTick) {
            return;
        }

        if (currentDirection == Direction.UP && newDirection == Direction.DOWN) {
            return;
        }
        if (currentDirection == Direction.DOWN && newDirection == Direction.UP) {
            return;
        }
        if (currentDirection == Direction.LEFT && newDirection == Direction.RIGHT) {
            return;
        }
        if (currentDirection == Direction.RIGHT && newDirection == Direction.LEFT) {
            return;
        }

        this.currentDirection = newDirection;
        this.directionChangedInThisTick = true;
    }

    /**
     * Получает текущее направление движения змейки.
     *
     * @return Направление.
     */
    public Direction getCurrentDirection() {
        return currentDirection;
    }

    /**
     * Устанавливает флаг роста.
     */
    public void grow() {
        this.growNextMove = true;
    }

    /**
     * Получает тело змейки.
     *
     * @return Список точек.
     */
    public LinkedList<Point> getBody() {
        return body;
    }

    /**
     * Проверяет столкновение с собой.
     *
     * @return true если столкновение есть.
     */
    public boolean checkSelfCollision() {
        Point head = body.getFirst();
        for (int i = 1; i < body.size(); i++) {
            if (head.equals(body.get(i))) {
                return true;
            }
        }
        return false;
    }
}
