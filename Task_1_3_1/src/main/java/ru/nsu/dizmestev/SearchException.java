package ru.nsu.dizmestev;

/**
 * Исключение, возникающее при ошибках поиска подстроки.
 */
public class SearchException extends TaskException {

    /**
     * Создаёт исключение с сообщением.
     *
     * @param message Сообщение об ошибке.
     */
    public SearchException(String message) {
        super(message);
    }

    /**
     * Создаёт исключение с сообщением и внутренней причиной.
     *
     * @param message Сообщение об ошибке.
     * @param cause   Внутренняя причина.
     */
    public SearchException(String message, Throwable cause) {
        super(message, cause);
    }
}
