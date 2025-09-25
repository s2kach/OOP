package ru.nsu.dizmestev;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.Test;

class MainTest {

    @Test
    void testStartGameWithValidInput() {
        // Подготавливаем входные данные
        String input = "1\n0\nq"; // Выбираем 1 колоду и сразу выходим
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);

        // Перехватываем вывод
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            Main.startGame();

            String output = outputStream.toString();
            assertTrue(output.contains("Добро пожаловать в Блэкджек!"));
            assertTrue(output.contains("Сколько колод карт используем в игре? (1-8):"));
        } finally {
            System.setIn(System.in);
            System.setOut(originalOut);
        }
    }

    @Test
    void testStartGameWithInvalidThenValidInput() {
        String input = "10\n2\n0\nq\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            Main.startGame();
            String output = outputStream.toString();
            assertTrue(output.contains("Неверный ввод. Пожалуйста, введите число от 1 до 8."));
        } finally {
            System.setIn(System.in);
            System.setOut(originalOut);
        }
    }
}