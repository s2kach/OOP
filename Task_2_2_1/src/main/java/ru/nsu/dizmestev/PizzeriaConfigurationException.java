package ru.nsu.dizmestev;

/**
 * Исключение, связанное с ошибками чтения или парсинга конфигурации.
 */
public class PizzeriaConfigurationException extends PizzeriaException {

    /**
     * Конструктор с сообщением и причиной.
     *
     * @param message сообщение об ошибке
     * @param cause исходное исключение
     */
    public PizzeriaConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}