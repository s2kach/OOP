package ru.nsu.dizmestev;

/**
 * Исключение, выбрасываемое при прерывании потоков ожидания или работы.
 */
public class PizzeriaInterruptedException extends PizzeriaException {

    /**
     * Конструктор с сообщением и причиной.
     *
     * @param message сообщение об ошибке
     * @param cause исходное исключение
     */
    public PizzeriaInterruptedException(String message, Throwable cause) {
        super(message, cause);
    }
}