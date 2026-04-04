package ru.nsu.dizmestev;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

/**
 * Тесты для проверки иерархии исключений.
 */
public class ExceptionsTest {

    @Test
    public void testExceptionWrapping() throws Exception {
        RuntimeException cause = new RuntimeException("Original cause");
        TaskExecutionException exception = new TaskExecutionException("Task failed", cause);

        assertEquals("Task failed", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    public void testAppException() throws Exception {
        AppException exception = new AppException("App error");
        assertNotNull(exception.getMessage());
    }
}
