package ru.nsu.dizmestev;

import java.util.Map;

/**
 * Класс для представления числовых констант в выражениях.
 */
public class Number extends Expression {

    public final int value;

    @Override
    public Expression simplify() {
        return this;
    }

    /**
     * Создает новую числовую константу.
     *
     * @param value Значение числа.
     */
    public Number(int value) {
        this.value = value;
    }

    /**
     * Возвращает строковое представление числа.
     *
     * @return Строковое представление числа.
     */
    @Override
    public String print() {
        return String.valueOf(value);
    }

    /**
     * Вычисляет производную числовой константы.
     * Производная константы всегда равна нулю.
     *
     * @param var Имя переменной (игнорируется для чисел).
     * @return Новый объект Number со значением 0.
     */
    @Override
    public Expression derivative(String var) {
        return new Number(0);
    }

    /**
     * Возвращает значение числовой константы.
     *
     * @param vars Карта значений переменных (игнорируется для чисел).
     * @return Значение числа.
     */
    @Override
    public int evaluate(Map<String, Integer> vars) {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Number number = (Number) obj;
        return value == number.value;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(value);
    }

}