package ru.nsu.dizmestev;

/**
 * Исключение при попытке добавить существующий ключ.
 */
public class DuplicateKeyException extends HashTableException {
    public DuplicateKeyException(String message) {
        super(message);
    }
}
