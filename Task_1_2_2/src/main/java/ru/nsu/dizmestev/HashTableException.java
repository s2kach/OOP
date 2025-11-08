package ru.nsu.dizmestev;

/**
 * Базовое исключение для ошибок хеш-таблицы.
 */
public class HashTableException extends Exception {
    /**
     * Создает исключение с сообщением.
     *
     * @param message Сообщение об ошибке.
     */
    public HashTableException(String message) {
        super(message);
    }

    /**
     * Создает исключение с сообщением и вложенным исключением.
     *
     * @param message Сообщение об ошибке.
     * @param cause Причина исключения.
     */
    public HashTableException(String message, Throwable cause) {
        super(message, cause);
    }
}
