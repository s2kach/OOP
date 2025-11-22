package ru.nsu.dizmestev;

/**
 * Базовое исключение для всех ошибок в задании.
 */
public class TaskException extends RuntimeException {

    /**
     * Создаёт исключение с сообщением.
     *
     * @param message Сообщение об ошибке.
     */
    public TaskException(String message) {
        super(message);
    }

    /**
     * Создаёт исключение с сообщением и внутренней причиной.
     *
     * @param message Сообщение об ошибке.
     * @param cause   Внутренняя причина.
     */
    public TaskException(String message, Throwable cause) {
        super(message, cause);
    }
}
