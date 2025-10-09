package ru.nsu.dizmestev;

import java.util.Map;

/**
 * Класс для представления операции вычитания двух выражений.
 */
public class Sub extends Expression {
    private final Expression left;
    private final Expression right;

    /**
     * Создает новую операцию вычитания.
     *
     * @param left Левое выражение.
     * @param right Правое выражение.
     * @throws ExpressionParseException Если left или right равны null.
     */
    public Sub(Expression left, Expression right) {
        if (left == null || right == null) {
            throw new ExpressionParseException("Аргументы операции вычитания не могут быть null.");
        }
        this.left = left;
        this.right = right;
    }

    /**
     * Возвращает строковое представление вычитания.
     *
     * @return Строка в формате "(left - right)".
     */
    @Override
    public String print() {
        return "(" + left.print() + "-" + right.print() + ")";
    }

    /**
     * Вычисляет производную разности.
     * Производная разности равна разности производных.
     *
     * @param var Имя переменной, по которой вычисляется производная.
     * @return Новое выражение Sub с производными левой и правой частей.
     */
    @Override
    public Expression derivative(String var) {
        return new Sub(left.derivative(var), right.derivative(var));
    }

    /**
     * Вычисляет значение разности выражений.
     *
     * @param vars Карта значений переменных.
     * @return Разность значений левого и правого выражений.
     */
    @Override
    public int evaluate(Map<String, Integer> vars) {
        return left.evaluate(vars) - right.evaluate(vars);
    }
}