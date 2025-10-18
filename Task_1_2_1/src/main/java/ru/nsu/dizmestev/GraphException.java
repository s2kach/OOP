package ru.nsu.dizmestev;

/**
 * Базовое исключение для ошибок графа.
 */
public class GraphException extends Exception {
    /**
     * Создает исключение с сообщением.
     *
     * @param message Сообщение об ошибке.
     */
    public GraphException(String message) {
        super(message);
    }

    /**
     * Создает исключение с сообщением и причиной.
     *
     * @param message Сообщение об ошибке.
     * @param cause   Причина исключения.
     */
    public GraphException(String message, Throwable cause) {
        super(message, cause);
    }
}
