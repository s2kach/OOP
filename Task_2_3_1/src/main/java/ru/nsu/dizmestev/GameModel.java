package ru.nsu.dizmestev;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Главный класс логики, управляющий правилами и состоянием игры.
 */
public class GameModel {

    private final int width;
    private final int height;
    private final int targetLength;
    private final int foodCount;
    private final int obstaclesCount;
    private final Random random;

    private Snake snake;
    private List<Point> foods;
    private List<Point> obstacles;
    private boolean isGameOver;
    private boolean isGameWon;

    /**
     * Инициализирует модель игры.
     *
     * @param width Ширина поля в клетках (N).
     * @param height Высота поля в клетках (M).
     * @param targetLength Целевая длина для победы (L).
     * @param foodCount Количество еды на поле (T).
     */
    public GameModel(int width, int height, int targetLength, int foodCount, int obstaclesCount) {
        this.width = width;
        this.height = height;
        this.targetLength = targetLength;
        this.foodCount = foodCount;
        this.obstaclesCount = obstaclesCount;
        this.random = new Random();
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
        spawnObstacles(startX, startY);
        spawnFood();
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

        if (snake.getBody().size() >= targetLength) {
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
            if (foods.get(i).equals(head)) {
                snake.grow();
                foods.remove(i);
                spawnFood();
                break;
            }
        }
    }

    /**
     * Генерирует препятствия.
     */
    private void spawnObstacles(int startX, int startY) {
        while (obstacles.size() < obstaclesCount) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            Point obs = new Point(x, y);

            boolean isOnSnakeLine = (y == startY && x >= startX);

            if (!snake.getBody().contains(obs) && !obstacles.contains(obs) && !isOnSnakeLine) {
                obstacles.add(obs);
            }
        }
    }

    /**
     * Генерирует недостающую еду на свободных клетках.
     */
    private void spawnFood() {
        while (foods.size() < foodCount) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            Point newFood = new Point(x, y);

            if (!snake.getBody().contains(newFood) && !foods.contains(newFood)
                    && !obstacles.contains(newFood)) {
                foods.add(newFood);
            }
        }
    }

    public Snake getSnake() {
        return snake;
    }

    public List<Point> getFoods() {
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
