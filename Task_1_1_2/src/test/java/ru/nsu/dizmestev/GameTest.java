package ru.nsu.dizmestev;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class GameTest {

    @Test
    void testGameInitialization() {
        Game game = new Game(1);
        assertNotNull(game);
    }

}