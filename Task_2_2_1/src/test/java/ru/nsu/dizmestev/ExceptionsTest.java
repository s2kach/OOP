package ru.nsu.dizmestev;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Тесты для проверки иерархии пользовательских исключений.
 */
public class ExceptionsTest {

    @Test
    public void testPizzeriaException() {
        Throwable cause = new RuntimeException("Исходная ошибка");
        PizzeriaException exception = new PizzeriaException("Произошла ошибка в пиццерии.", cause);

        assertEquals("Произошла ошибка в пиццерии.", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    public void testPizzeriaConfigurationException() {
        Throwable cause = new IllegalArgumentException("Неверный аргумент");
        PizzeriaConfigurationException exception =
                new PizzeriaConfigurationException("Ошибка конфига.", cause);

        assertEquals("Ошибка конфига.", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    public void testPizzeriaInterruptedException() {
        Throwable cause = new InterruptedException("Поток прерван");
        PizzeriaInterruptedException exception =
                new PizzeriaInterruptedException("Поток ожидания прерван.", cause);

        assertEquals("Поток ожидания прерван.", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }
}