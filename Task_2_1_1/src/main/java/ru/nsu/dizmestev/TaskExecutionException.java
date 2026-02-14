package ru.nsu.dizmestev;

/**
 * Исключение, возникающее при выполнении конкретной задачи поиска.
 */
public class TaskExecutionException extends AppException {

    /**
     * Конструктор с сообщением и причиной.
     *
     * @param message Сообщение об ошибке.
     * @param cause Причина исключения для оборачивания.
     */
    public TaskExecutionException(String message, Throwable cause) {
        super(message, cause);
    }
}
