package ru.nsu.dizmestev;

/**
 * Исключение для ошибок проверки диплома.
 */
public class DiplomaCheckException extends AcademicBaseException {

    /**
     * Создает исключение.
     *
     * @param message Сообщение.
     * @param cause Причина.
     */
    public DiplomaCheckException(String message, Exception cause) {
        super(message, cause);
    }
}