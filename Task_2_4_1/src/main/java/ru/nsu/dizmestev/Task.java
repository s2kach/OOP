package ru.nsu.dizmestev;

import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;

/**
 * Класс лабораторной работы.
 */
public class Task {
    private final String id;
    private final int maxPoints;
    private final String name;
    private final LocalDate hardDeadline;
    private final LocalDate softDeadline;

    /**
     * Конструктор задачи.
     *
     * @param id Идентификатор (соответствует имени папки, например Task_1_1_1).
     * @param name Название задачи.
     * @param maxPoints Максимальный балл.
     * @param softDeadline Мягкий дедлайн.
     * @param hardDeadline Жесткий дедлайн.
     */
    public Task(String id, String name,
                int maxPoints, LocalDate softDeadline, LocalDate hardDeadline) {
        this.id = id;
        this.name = name;
        this.maxPoints = maxPoints;
        this.softDeadline = softDeadline;
        this.hardDeadline = hardDeadline;
    }

    /**
     * Получает идентификатор.
     *
     * @return Идентификатор задачи.
     */
    public String getId() {
        return id;
    }

    /**
     * Получает максимальный балл.
     *
     * @return Балл.
     */
    public int getMaxPoints() {
        return maxPoints;
    }

    public ChronoLocalDate getHardDeadline() {
        return hardDeadline;
    }

    public ChronoLocalDate getSoftDeadline() {
        return softDeadline;
    }
}
