package ru.nsu.dizmestev;

import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

/**
 * Тест логики.
 */
class GameModelTest {

    @Test
    void NoSpawnKillTest() {
        GameModel gm = new GameModel(20, 20, 15, 3, 5);
        gm.tick();
        assertFalse(gm.isGameOver());
    }
}
