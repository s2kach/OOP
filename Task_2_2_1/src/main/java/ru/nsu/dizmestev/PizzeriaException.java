package ru.nsu.dizmestev;

/**
 * Базовый класс исключений для симулятора пиццерии.
 */
public class PizzeriaException extends Exception {

    /**
     * Конструктор с сообщением и причиной.
     *
     * @param message сообщение об ошибке
     * @param cause исходное исключение
     */
    public PizzeriaException(String message, Throwable cause) {
        super(message, cause);
    }
}
