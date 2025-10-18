package ru.nsu.dizmestev;

/**
 * Исключение для всех ошибок, связанных с математическими вычислениями.
 */
public class ExpressionEvaluateException extends ExpressionException {

    /**
     * Создает новое исключение с сообщением.
     *
     * @param message Сообщение об ошибке.
     */
    public ExpressionEvaluateException(String message) {
        super(message);
    }

    /**
     * Создает новое исключение с сообщением и причиной.
     *
     * @param message Сообщение об ошибке.
     * @param cause Исходное исключение.
     */
    public ExpressionEvaluateException(String message, Throwable cause) {
        super(message, cause);
    }
}