package ru.nsu.dizmestev;

import java.time.LocalDate;

/**
 * Класс, представляющий контрольную точку.
 */
public class Checkpoint {
    private final String name;
    private final LocalDate date;

    /**
     * Конструктор контрольной точки.
     *
     * @param name Название (например, "КТ 1").
     * @param date Дата проведения.
     */
    public Checkpoint(String name, LocalDate date) {
        this.name = name;
        this.date = date;
    }

    public String getName() { return name; }

    public LocalDate getDate() { return date; }
}
