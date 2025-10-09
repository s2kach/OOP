package ru.nsu.dizmestev;

import java.util.Map;

/**
 * Класс для представления операции деления двух выражений.
 */
public class Div extends Expression {
    private final Expression left;
    private final Expression right;

    /**
     * Создает новую операцию деления.
     *
     * @param left Левое выражение (числитель).
     * @param right Правое выражение (знаменатель).
     * @throws ExpressionParseException Если left или right равны null.
     */
    public Div(Expression left, Expression right) {
        if (left == null || right == null) {
            throw new ExpressionParseException("Аргументы операции деления не могут быть null.");
        }
        this.left = left;
        this.right = right;
    }

    /**
     * Возвращает строковое представление деления.
     *
     * @return Строка в формате "(left / right)".
     */
    @Override
    public String print() {
        return "(" + left.print() + "/" + right.print() + ")";
    }

    /**
     * Вычисляет производную частного.
     * Использует правило производной частного: (u/v)' = (u'v - uv') / v².
     *
     * @param var Имя переменной, по которой вычисляется производная.
     * @return Новое выражение Div с результатом применения правила частного.
     */
    @Override
    public Expression derivative(String var) {
        return new Div(
                new Sub(
                        new Mul(left.derivative(var), right),
                        new Mul(left, right.derivative(var))
                ),
                new Mul(right, right)
        );
    }

    /**
     * Вычисляет значение частного выражений.
     *
     * @param vars Карта значений переменных.
     * @return Частное значений левого и правого выражений.
     * @throws ExpressionEvaluateException Если знаменатель равен нулю.
     */
    @Override
    public int evaluate(Map<String, Integer> vars) {
        int denominator = right.evaluate(vars);
        if (denominator == 0) {
            throw new ExpressionEvaluateException("Деление на ноль в выражении: " + this.print());
        }
        return left.evaluate(vars) / denominator;
    }
}