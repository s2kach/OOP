package ru.nsu.dizmestev;

/**
 * Исключение для ошибок проверки диплома.
 */
public class DiplomaCheckException extends AcademicBaseException {

    /**
     * Создает исключение.
     */
    public DiplomaCheckException(String message, Exception cause) {
        super(message, cause);
    }
}