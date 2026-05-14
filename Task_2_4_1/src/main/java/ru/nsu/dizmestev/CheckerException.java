package ru.nsu.dizmestev;

/**
 * Базовое исключение для всех ошибок системы проверки.
 */
public class CheckerException extends Exception {

    /**
     * Конструктор с сообщением и причиной.
     *
     * @param message Описание ошибки.
     * @param cause Исходное исключение.
     */
    public CheckerException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Конструктор с сообщением и без причины свыше.
     *
     * @param message Описание ошибки.
     */
    public CheckerException(String message) {
        super(message);
    }
}
