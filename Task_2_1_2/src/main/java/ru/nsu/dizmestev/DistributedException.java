package ru.nsu.dizmestev;

/**
 * Базовое исключение для распределенной системы поиска простых чисел.
 */
public class DistributedException extends Exception {

    /**
     * Создает исключение с указанным сообщением.
     *
     * @param message Сообщение об ошибке.
     */
    public DistributedException(String message) {
        super(message);
    }

    /**
     * Создает исключение с сообщением и исходной причиной.
     *
     * @param message Сообщение об ошибке.
     * @param cause Исходное исключение (оборачивание).
     */
    public DistributedException(String message, Throwable cause) {
        super(message, cause);
    }
}