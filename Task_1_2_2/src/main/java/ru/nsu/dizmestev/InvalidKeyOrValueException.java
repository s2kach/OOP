package ru.nsu.dizmestev;

/**
 * Исключение при попытке использовать недопустимый ключ или значение.
 */
public class InvalidKeyOrValueException extends HashTableException {
    public InvalidKeyOrValueException(String message) {
        super(message);
    }
}
