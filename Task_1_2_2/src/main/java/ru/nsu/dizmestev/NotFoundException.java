package ru.nsu.dizmestev;

/**
 * Исключение, возникающее при отсутствии ключа.
 */
public class NotFoundException extends HashTableException {
    public NotFoundException(String message) {
        super(message);
    }
}
