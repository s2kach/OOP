package ru.nsu.dizmestev;

/**
 * Исключение для отсутствия оценок.
 */
public class EmptyRecordException extends AcademicBaseException {

    /**
     * Создает исключение.
     *
     * @param message Сообщение.
     */
    public EmptyRecordException(String message) {
        super(message);
    }
}