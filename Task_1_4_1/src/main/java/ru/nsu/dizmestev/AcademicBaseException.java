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
}