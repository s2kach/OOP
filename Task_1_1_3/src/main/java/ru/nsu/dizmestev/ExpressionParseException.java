package ru.nsu.dizmestev;

/**
 * Исключение для всех ошибок, связанных с парсингом математических выражений.
 */
public class ExpressionParseException extends ExpressionException {

    /**
     * Создает новое исключение с сообщением.
     *
     * @param message Сообщение об ошибке.
     */
    public ExpressionParseException(String message) {
        super(message);
    }

    /**
     * Создает новое исключение с сообщением и причиной.
     *
     * @param message Сообщение об ошибке.
     * @param cause Исходное исключение.
     */
    public ExpressionParseException(String message, Throwable cause) {
        super(message, cause);
    }
}