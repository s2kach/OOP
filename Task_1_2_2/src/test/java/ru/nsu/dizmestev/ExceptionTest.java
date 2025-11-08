package ru.nsu.dizmestev;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Тестирование классов исключений.
 */
class ExceptionTest {

    @Test
    void testHashTableException() {
        HashTableException exception = new HashTableException("Test message");
        assertEquals("Test message", exception.getMessage());

        Throwable cause = new RuntimeException();
        HashTableException exceptionWithCause = new HashTableException("Test", cause);
        assertEquals("Test", exceptionWithCause.getMessage());
        assertEquals(cause, exceptionWithCause.getCause());
    }

    @Test
    void testDuplicateKeyException() {
        DuplicateKeyException exception = new DuplicateKeyException("Duplicate");
        assertEquals("Duplicate", exception.getMessage());
    }

    @Test
    void testInvalidKeyOrValueException() {
        InvalidKeyOrValueException exception = new InvalidKeyOrValueException("Invalid");
        assertEquals("Invalid", exception.getMessage());
    }

    @Test
    void testNotFoundException() {
        NotFoundException exception = new NotFoundException("Not found");
        assertEquals("Not found", exception.getMessage());
    }
}