package ru.nsu.dizmestev;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Тест логики.
 */
class GameModelTest {

    @Test
    void testInitialization() {
        GameModel model = new GameModel(20, 20, new TargetLengthWinCondition(10), 3, 5);

        assertEquals(3, model.getFoods().size());
        assertEquals(5, model.getObstacles().size());
        assertFalse(model.isGameOver());
        assertFalse(model.isGameWon());
    }

    @Test
    void testWallCollision() {
        GameModel model = new GameModel(10, 10, new TargetLengthWinCondition(10), 0, 0);
        for (int i = 0; i < 10; i++) {
            model.tick();
        }
        assertTrue(model.isGameOver());
    }

    @Test
    void testResetGame() {
        GameModel model = new GameModel(20, 20, new TargetLengthWinCondition(15), 3, 5);
        model.tick();
        model.resetGame();

        assertFalse(model.isGameOver());
        assertEquals(3, model.getFoods().size());
    }

    @Test
    void testNoSpawnKill() {
        GameModel gm = new GameModel(20, 20, new TargetLengthWinCondition(15), 3, 50);
        gm.tick();
        assertFalse(gm.isGameOver());
    }

    @Test
    void testWinCondition() {
        GameModel gm = new GameModel(20, 20, new TargetLengthWinCondition(5), 0, 0);
        Snake snake = gm.getSnake();
        snake.grow();
        gm.tick();
        assertFalse(gm.isGameWon());
        snake.grow();
        gm.tick();
        assertTrue(gm.isGameWon());
    }

    @Test
    void testSimpleFoodEffect() {
        GameModel model = new GameModel(20, 20, new TargetLengthWinCondition(5), 0, 0);
        Snake snake = model.getSnake();
        int initialSize = snake.getBody().size();

        Point nextHead = snake.calculateNextHead();

        SimpleFood food = new SimpleFood(nextHead);
        model.getFoods().add(food);

        model.tick();
        model.tick();

        assertEquals(initialSize + 1, snake.getBody().size());
        assertTrue(model.getFoods().isEmpty());
    }

    @Test
    void testFoodSpawning() {
        GameModel model = new GameModel(20, 20, new TargetLengthWinCondition(10), 1, 0);
        Point head = model.getSnake().getHead();

        model.getFoods().clear();
        Point foodPos = new Point(head.getX() + 1, head.getY());
        model.getFoods().add(new SimpleFood(foodPos));

        model.tick();

        assertEquals(1, model.getFoods().size());
        assertNotEquals(foodPos, model.getFoods().get(0).getPosition());
    }
}
