package ru.nsu.dizmestev;

import java.io.Serializable;

/**
 * Класс-запрос, представляющий команду или данные для обработки на стороне воркера.
 */
public class TaskRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Перечисление доступных типов команд для взаимодействия мастер-узла и воркера.
     */
    public enum Type {
        /** Задача на проверку чисел. */
        TASK,
        /** Проверка соединения (Heartbeat). */
        PING,
        /** Сигнал для корректного завершения работы. */
        SHUTDOWN
    }

    private final Type type;
    private final int[] chunk;

    /**
     * Конструктор для создания запроса.
     *
     * @param type Тип команды (TASK, PING или SHUTDOWN).
     * @param chunk Массив чисел для проверки, если команда TASK, иначе null.
     */
    public TaskRequest(Type type, int[] chunk) {
        this.type = type;
        this.chunk = chunk;
    }

    /**
     * Возвращает тип текущей команды.
     *
     * @return Тип команды запроса.
     */
    public Type getType() {
        return type;
    }

    /**
     * Возвращает массив данных для проверки.
     *
     * @return Массив целых чисел или null, если команда не требует данных.
     */
    public int[] getChunk() {
        return chunk;
    }
}
