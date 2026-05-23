package ru.nsu.dizmestev;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;


/**
 * Тесты для классов данных, исключений и интерфейса командной строки.
 */
class TaskTest {

    @Test
    void testTaskRequest() {
        int[] chunk = {1, 2, 3};
        TaskRequest request = new TaskRequest(TaskRequest.Type.TASK, chunk);

        assertEquals(TaskRequest.Type.TASK, request.getType());
        assertArrayEquals(chunk, request.getChunk());

        TaskRequest shutdown = new TaskRequest(TaskRequest.Type.SHUTDOWN, null);
        assertNull(shutdown.getChunk());
    }

    @Test
    void testTaskResponse() {
        TaskResponse response = new TaskResponse(true);
        assertTrue(response.isFoundNonPrime());
    }

    @Test
    void testExceptions() {
        Exception cause = new RuntimeException("cause");
        DistributedException ex1 = new DistributedException("error");
        DistributedException ex2 = new DistributedException("error", cause);
        NetworkException ex3 = new NetworkException("net error", cause);

        assertEquals("error", ex1.getMessage());
        assertEquals(cause, ex2.getCause());
        assertEquals("net error", ex3.getMessage());
    }
}