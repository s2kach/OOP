package ru.nsu.dizmestev;

import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    @Test
    void testGameInitialization() {
        Game game = new Game(1);
        assertNotNull(game);
    }
}