package ru.nsu.dizmestev;

/**
 * Базовое исключение учебного модуля.
 */
public class AcademicBaseException extends Exception {

    /**
     * Создает исключение с сообщением.
     *
     * @param message Сообщение.
     */
    public AcademicBaseException(String message) {
        super(message);
    }

    /**
     * Создает исключение с сообщением и причиной.
     *
     * @param message Сообщение.
     * @param cause Причина.
     */
    public AcademicBaseException(String message, Exception cause) {
        super(message, cause);
    }
}