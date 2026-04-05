package ru.nsu.dizmestev;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Тест логики.
 */
class GameModelTest {

    @Test
    void testInitialization() {
        GameModel model = new GameModel(20, 20, 10, 3, 5, new TargetLengthWinCondition(15));

        assertEquals(3, model.getFoods().size());
        assertEquals(5, model.getObstacles().size());
        assertFalse(model.isGameOver());
        assertFalse(model.isGameWon());
    }

    @Test
    void testWallCollision() {
        GameModel model = new GameModel(10, 10, 10, 0, 0, new TargetLengthWinCondition(15));
        for (int i = 0; i < 10; i++) {
            model.tick();
        }
        assertTrue(model.isGameOver());
    }

    @Test
    void testResetGame() {
        GameModel model = new GameModel(20, 20, 10, 3, 5, new TargetLengthWinCondition(15));
        model.tick();
        model.resetGame();

        assertFalse(model.isGameOver());
        assertEquals(3, model.getFoods().size());
    }

    @Test
    void testNoSpawnKill() {
        GameModel gm = new GameModel(20, 20, 15, 3, 5, new TargetLengthWinCondition(15));
        gm.tick();
        assertFalse(gm.isGameOver());
    }

    @Test
    void testWinCondition() {
        GameModel gm = new GameModel(20, 20, 15, 3, 5, new TargetLengthWinCondition(5));
        Snake snake = gm.getSnake();
        snake.grow();
        gm.tick();
        assertFalse(gm.isGameWon());
        snake.grow();
        gm.tick();
        assertTrue(gm.isGameWon());
    }
}
