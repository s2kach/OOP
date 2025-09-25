package ru.nsu.dizmestev;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Scanner;
import org.junit.jupiter.api.Test;

class PlayerTest {

    @Test
    void testShouldTakeCardWhenInputIs1() {
        String input = "1\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(in);
        Player player = new Player(scanner);
        assertTrue(player.shouldTakeCard());
        scanner.close();
    }

    @Test
    void testShouldNotTakeCardWhenInputIs0() {
        String input = "0\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(in);
        Player player = new Player(scanner);
        assertFalse(player.shouldTakeCard());
        scanner.close();
    }

    @Test
    void testShouldHandleInvalidInputThenValid() {
        String input = "5\n1\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(in);
        Player player = new Player(scanner);
        assertTrue(player.shouldTakeCard());
        scanner.close();
    }

    @Test
    void testShouldHandleMultipleInvalidInputs() {
        String input = "abc\n2\n1\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(in);
        Player player = new Player(scanner);
        assertTrue(player.shouldTakeCard());
        scanner.close();
    }
}