package ru.nsu.dizmestev;

/**
 * Базовое исключение для всех ошибок, связанных с математическими выражениями.
 */
public abstract class ExpressionException extends RuntimeException {

    /**
     * Создает новое исключение с сообщением.
     *
     * @param message Сообщение об ошибке.
     */
    protected ExpressionException(String message) {
        super(message);
    }

    /**
     * Создает новое исключение с сообщением и причиной.
     *
     * @param message Сообщение об ошибке.
     * @param cause Исходное исключение.
     */
    protected ExpressionException(String message, Throwable cause) {
        super(message, cause);
    }
}