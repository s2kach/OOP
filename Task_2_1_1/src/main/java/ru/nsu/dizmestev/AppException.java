package ru.nsu.dizmestev;

/**
 * Базовое исключение для приложения.
 */
public class AppException extends Exception {

    /**
     * Конструктор с сообщением.
     *
     * @param message Сообщение об ошибке.
     */
    public AppException(String message) {
        super(message);
    }

    /**
     * Конструктор с сообщением и причиной.
     *
     * @param message Сообщение об ошибке.
     * @param cause Причина исключения (wrapping).
     */
    public AppException(String message, Throwable cause) {
        super(message, cause);
    }
}
