package ru.nsu.dizmestev;

import java.util.Map;

/**
 * Класс для представления операции сложения двух выражений.
 */
public class Add extends Expression {
    private final Expression left;
    private final Expression right;

    /**
     * Создает новую операцию сложения.
     *
     * @param left Левое выражение.
     * @param right Правое выражение.
     * @throws IllegalArgumentException Если left или right равны null.
     */
    public Add(Expression left, Expression right) {
        if (left == null || right == null) {
            throw new IllegalArgumentException("Аргументы операции сложения не могут быть null.");
        }
        this.left = left;
        this.right = right;
    }

    /**
     * Возвращает строковое представление сложения.
     *
     * @return Строка в формате "(left + right)".
     */
    @Override
    public String print() {
        return "(" + left.print() + "+" + right.print() + ")";
    }

    /**
     * Вычисляет производную суммы.
     * Производная суммы равна сумме производных.
     *
     * @param var Имя переменной, по которой вычисляется производная.
     * @return Новое выражение Add с производными левой и правой частей.
     */
    @Override
    public Expression derivative(String var) {
        return new Add(left.derivative(var), right.derivative(var));
    }

    /**
     * Вычисляет значение суммы выражений.
     *
     * @param vars Карта значений переменных.
     * @return Сумма значений левого и правого выражений.
     */
    @Override
    public int evaluate(Map<String, Integer> vars) {
        return left.evaluate(vars) + right.evaluate(vars);
    }
}