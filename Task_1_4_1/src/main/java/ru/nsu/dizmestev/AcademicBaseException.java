package ru.nsu.dizmestev;

/**
 * Базовое исключение учебного модуля.
 */
public class AcademicBaseException extends Exception {

    /**
     * Создает исключение с сообщением.
     */
    public AcademicBaseException(String message) {
        super(message);
    }

    /**
     * Создает исключение с сообщением и причиной.
     */
    public AcademicBaseException(String message, Exception cause) {
        super(message, cause);
    }
}