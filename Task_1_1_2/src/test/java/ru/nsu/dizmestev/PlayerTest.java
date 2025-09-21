package ru.nsu.dizmestev;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

class PlayerTest {

    @Test
    void testShouldTakeCardWhenInputIs1() {
        String input = "1\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        Player player = new Player();
        assertTrue(player.shouldTakeCard());
    }

    @Test
    void testShouldNotTakeCardWhenInputIs0() {
        String input = "0\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        Player player = new Player();
        assertFalse(player.shouldTakeCard());
    }

    @Test
    void testShouldHandleInvalidInput() {
        String input = "5\n1\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        Player player = new Player();
        assertTrue(player.shouldTakeCard());
    }
}