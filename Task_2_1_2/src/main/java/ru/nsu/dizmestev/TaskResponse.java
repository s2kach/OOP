package ru.nsu.dizmestev;

import java.io.Serializable;

/**
 * Класс-ответ, возвращающий результат вычислений от воркера к мастеру.
 */
public class TaskResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private final boolean foundNonPrime;

    /**
     * Конструктор для создания ответа.
     *
     * @param foundNonPrime Флаг, указывающий, найдено ли составное число.
     */
    public TaskResponse(boolean foundNonPrime) {
        this.foundNonPrime = foundNonPrime;
    }

    /**
     * Возвращает результат проверки массива.
     *
     * @return Значение true, если составное число найдено.
     */
    public boolean isFoundNonPrime() {
        return foundNonPrime;
    }
}
