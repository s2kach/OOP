package ru.nsu.dizmestev;

/**
 * Исключение, возникающее при ошибках сетевого взаимодействия.
 */
public class NetworkException extends Exception {

    /**
     * Создает сетевое исключение с сообщением и причиной.
     *
     * @param message Сообщение об ошибке.
     * @param cause Исходное исключение.
     */
    public NetworkException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Создает сетевое исключение с сообщением.
     *
     * @param message Сообщение об ошибке.
     */
    public NetworkException(String message) {
        super(message);
    }
}
