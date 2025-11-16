package ru.nsu.dizmestev;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.Test;

class TaskExceptionTest {

    @Test
    void testTaskExceptionWithMessage() {
        TaskException exception = new TaskException("Test message");
        assertEquals("Test message", exception.getMessage());
    }

    @Test
    void testTaskExceptionWithMessageAndCause() {
        Throwable cause = new IllegalArgumentException("Cause");
        TaskException exception = new TaskException("Test message", cause);
        assertEquals("Test message", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void testFileReadExceptionWithMessage() {
        FileReadException exception = new FileReadException("Test message");
        assertEquals("Test message", exception.getMessage());
    }

    @Test
    void testFileReadExceptionWithMessageAndCause() {
        Throwable cause = new IOException("Cause");
        FileReadException exception = new FileReadException("Test message", cause);
        assertEquals("Test message", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void testSearchExceptionWithMessage() {
        SearchException exception = new SearchException("Test message");
        assertEquals("Test message", exception.getMessage());
    }

    @Test
    void testSearchExceptionWithMessageAndCause() {
        Throwable cause = new FileReadException("Cause");
        SearchException exception = new SearchException("Test message", cause);
        assertEquals("Test message", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }
}