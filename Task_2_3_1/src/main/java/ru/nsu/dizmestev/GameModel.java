package ru.nsu.dizmestev;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Главный класс логики, управляющий правилами и состоянием игры.
 */
public class GameModel {
    private final FieldSpawner spawner;
    private final int width;
    private final int height;
    private final int foodCount;
    private final int obstaclesCount;

    private final WinCondition winCondition;

    private Snake snake;
    private List<Food> foods;
    private List<Point> obstacles;
    private boolean isGameOver;
    private boolean isGameWon;

    /**
     * Инициализирует модель игры.
     *
     * @param width Ширина поля в клетках (N).
     * @param height Высота поля в клетках (M).
     * @param winCondition Условия победы.
     * @param foodCount Количество еды на поле (T).
     * @param obstaclesCount Количество препятствий на поле.
     */
    public GameModel(int width, int height, WinCondition winCondition,
                     int foodCount, int obstaclesCount) {
        this.spawner = new FieldSpawner(width, height);
        this.width = width;
        this.height = height;
        this.foodCount = foodCount;
        this.obstaclesCount = obstaclesCount;
        this.winCondition = winCondition;
        this.foods = new ArrayList<>();
        this.obstacles = new ArrayList<>();
        resetGame();
    }

    /**
     * Сбрасывает игру в начальное состояние.
     */
    public void resetGame() {
        int startX = width / 4;
        int startY = height / 2;

        snake = new Snake(startX, startY);
        foods.clear();
        obstacles.clear();
        isGameOver = false;
        isGameWon = false;
        obstacles = spawner.generateObstacles(obstaclesCount, snake);
        while (foods.size() < foodCount) {
            foods.add(spawner.spawnFood(snake, obstacles, foods));
        }
    }

    /**
     * Выполняет один игровой такт (тик).
     */
    public void tick() {
        if (isGameOver || isGameWon) {
            return;
        }

        Point nextHead = snake.calculateNextHead();

        if (nextHead.getX() < 0 || nextHead.getX() >= width
                || nextHead.getY() < 0 || nextHead.getY() >= height) {
            isGameOver = true;
            return;
        }

        if (obstacles.contains(nextHead) || snake.getBody().contains(nextHead)) {
            isGameOver = true;
            return;
        }

        snake.move();

        checkFoodCollision(snake.getBody().getFirst());

        if (winCondition.isWon(snake)) {
            isGameWon = true;
        }
    }

    /**
     * Проверяет столкновение головы змейки с едой.
     *
     * @param head Координаты головы.
     */
    private void checkFoodCollision(Point head) {
        for (int i = 0; i < foods.size(); i++) {
            Food food = foods.get(i);
            if (food.getPosition().equals(head)) {
                food.applyEffect(snake);
                foods.remove(i);
                while (foods.size() < foodCount) {
                    foods.add(spawner.spawnFood(snake, obstacles, foods));
                }
                break;
            }
        }
    }

    public Snake getSnake() {
        return snake;
    }

    public List<Food> getFoods() {
        return foods;
    }

    public List<Point> getObstacles() {
        return obstacles;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public boolean isGameWon() {
        return isGameWon;
    }
}
