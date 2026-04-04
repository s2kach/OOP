package ru.nsu.dizmestev;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Контроллер для связи интерфейса FXML и игровой модели.
 */
public class GameController implements Initializable {

    private static final int TILE_SIZE = 20;
    private static final int GRID_WIDTH = 20;
    private static final int GRID_HEIGHT = 20;

    private static final Color COLOR_GRASS_LIGHT = Color.web("aad751");
    private static final Color COLOR_GRASS_DARK = Color.web("a2d149");
    private static final Color COLOR_OBSTACLE = Color.SADDLEBROWN;
    private static final Color COLOR_FOOD = Color.RED;
    private static final Color COLOR_SNAKE_BODY = Color.web("4674e9");
    private static final Color COLOR_SNAKE_HEAD = Color.web("578aef");
    private static final Color COLOR_EYE = Color.WHITE;

    @FXML
    private Canvas gameCanvas;

    private GameModel model;
    private AnimationTimer timer;
    private long lastUpdate = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        model = new GameModel(GRID_WIDTH, GRID_HEIGHT, 15, 3, 5);

        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (now - lastUpdate >= 150_000_000) {
                    model.tick();
                    draw();
                    lastUpdate = now;
                }
            }
        };
        timer.start();
    }

    public void handleKeyPressed(KeyEvent event) {
        Snake snake = model.getSnake();
        switch (event.getCode()) {
            case W, UP -> snake.setDirection(Direction.UP);
            case S, DOWN -> snake.setDirection(Direction.DOWN);
            case A, LEFT -> snake.setDirection(Direction.LEFT);
            case D, RIGHT -> snake.setDirection(Direction.RIGHT);
            case R -> {
                model.resetGame();
                timer.start();
                lastUpdate = 0;
            }
            default -> { }
        }
    }

    /**
     * Отрисовывает текущее состояние модели на холсте.
     */
    private void draw() {
        GraphicsContext gc = gameCanvas.getGraphicsContext2D();

        drawBackground(gc);
        drawObstacles(gc);
        drawFood(gc);
        drawSnake(gc);

        if (model.isGameOver() || model.isGameWon()) {
            timer.stop();
            drawGameOverScreen(gc);
        }
    }

    /**
     * Рисует шахматное поле.
     */
    private void drawBackground(GraphicsContext gc) {
        for (int y = 0; y < GRID_HEIGHT; y++) {
            for (int x = 0; x < GRID_WIDTH; x++) {
                if ((x + y) % 2 == 0) {
                    gc.setFill(COLOR_GRASS_LIGHT);
                } else {
                    gc.setFill(COLOR_GRASS_DARK);
                }
                gc.fillRect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
        }
    }

    /**
     * Рисует коричневые препятствия.
     */
    private void drawObstacles(GraphicsContext gc) {
        gc.setFill(COLOR_OBSTACLE);
        for (Point obs : model.getObstacles()) {
            double margin = TILE_SIZE * 0.1;
            gc.fillRect(obs.getX() * TILE_SIZE + margin,
                    obs.getY() * TILE_SIZE + margin,
                    TILE_SIZE - 2 * margin,
                    TILE_SIZE - 2 * margin);
        }
    }

    /**
     * Рисует яблоки.
     */
    private void drawFood(GraphicsContext gc) {
        gc.setFill(COLOR_FOOD);
        for (Point food : model.getFoods()) {
            double margin = TILE_SIZE * 0.1;
            gc.fillOval(food.getX() * TILE_SIZE + margin,
                    food.getY() * TILE_SIZE + margin,
                    TILE_SIZE - 2 * margin,
                    TILE_SIZE - 2 * margin);
        }
    }

    /**
     * Рисует змейку.
     */
    private void drawSnake(GraphicsContext gc) {
        Snake snake = model.getSnake();
        java.util.LinkedList<Point> body = snake.getBody();

        if (body.isEmpty()) {
            return;
        }

        gc.setFill(COLOR_SNAKE_BODY);
        for (int i = 1; i < body.size(); i++) {
            Point p = body.get(i);
            gc.fillRoundRect(p.getX() * TILE_SIZE + 1,
                    p.getY() * TILE_SIZE + 1,
                    TILE_SIZE - 2,
                    TILE_SIZE - 2,
                    TILE_SIZE * 0.3,
                    TILE_SIZE * 0.3);
        }

        Point head = body.getFirst();
        gc.setFill(COLOR_SNAKE_HEAD);
        gc.fillRoundRect(head.getX() * TILE_SIZE,
                head.getY() * TILE_SIZE,
                TILE_SIZE,
                TILE_SIZE,
                TILE_SIZE * 0.5,
                TILE_SIZE * 0.5);

        drawEyes(gc, head, snake.getCurrentDirection());
    }

    /**
     * Вспомогательный метод для отрисовки двух точек-глаз на голове.
     */
    private void drawEyes(GraphicsContext gc, Point head, Direction direction) {
        gc.setFill(COLOR_EYE);
        double eyeSize = TILE_SIZE * 0.2;
        double offset = TILE_SIZE * 0.25;

        double x = head.getX() * TILE_SIZE;
        double y = head.getY() * TILE_SIZE;

        double eye1X;
        double eye1Y;
        double eye2X;
        double eye2Y;

        switch (direction) {
            case UP -> {
                eye1X = x + offset;
                eye2X = x + TILE_SIZE - offset - eyeSize;
                eye1Y = eye2Y = y + offset;
            }
            case DOWN -> {
                eye1X = x + offset;
                eye2X = x + TILE_SIZE - offset - eyeSize;
                eye1Y = eye2Y = y + TILE_SIZE - offset - eyeSize;
            }
            case LEFT -> {
                eye1X = eye2X = x + offset;
                eye1Y = y + offset;
                eye2Y = y + TILE_SIZE - offset - eyeSize;
            }
            case RIGHT -> {
                eye1X = eye2X = x + TILE_SIZE - offset - eyeSize;
                eye1Y = y + offset;
                eye2Y = y + TILE_SIZE - offset - eyeSize;
            }
            default -> {
                return;
            }
        }

        gc.fillOval(eye1X, eye1Y, eyeSize, eyeSize);
        gc.fillOval(eye2X, eye2Y, eyeSize, eyeSize);
    }

    /**
     * Отрисовывает экран окончания игры поверх поля.
     */
    private void drawGameOverScreen(GraphicsContext gc) {
        gc.setFill(new Color(0, 0, 0, 0.5));
        gc.fillRect(0, 0, gameCanvas.getWidth(), gameCanvas.getHeight());

        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Verdana", FontWeight.BOLD, 24));

        String title = model.isGameWon() ? "ПОБЕДА!" : "ПОРАЖЕНИЕ!";
        String subtitle = "Нажми R для рестарта";

        gc.fillText(title, 110, 180);
        gc.setFont(Font.font("Verdana", FontWeight.NORMAL, 16));
        gc.fillText(subtitle, 115, 210);
    }
}
