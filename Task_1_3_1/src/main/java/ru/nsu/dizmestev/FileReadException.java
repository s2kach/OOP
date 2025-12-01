package ru.nsu.dizmestev;

/**
 * Исключение, возникающее при ошибке чтения файла.
 */
public class FileReadException extends TaskException {

    /**
     * Создаёт исключение с сообщением.
     *
     * @param message Сообщение об ошибке.
     */
    public FileReadException(String message) {
        super(message);
    }

    /**
     * Создаёт исключение с сообщением и внутренней причиной.
     *
     * @param message Сообщение об ошибке.
     * @param cause   Внутренняя причина.
     */
    public FileReadException(String message, Throwable cause) {
        super(message, cause);
    }
}
