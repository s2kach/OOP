package ru.nsu.dizmestev;

import java.util.Map;

/**
 * Класс для представления операции умножения двух выражений.
 */
public class Mul extends Expression {
    public final Expression left;
    public final Expression right;

    @Override
    public Expression simplify() {
        Expression leftSimplified = left.simplify();
        Expression rightSimplified = right.simplify();

        if (leftSimplified instanceof Number && rightSimplified instanceof Number) {
            int leftValue = ((Number) leftSimplified).value;
            int rightValue = ((Number) rightSimplified).value;
            return new Number(leftValue * rightValue);
        }

        if ((leftSimplified instanceof Number && ((Number) leftSimplified).value == 0)
                || (rightSimplified instanceof Number && ((Number) rightSimplified).value == 0)) {
            return new Number(0);
        }

        if (leftSimplified instanceof Number && ((Number) leftSimplified).value == 1) {
            return rightSimplified;
        }

        if (rightSimplified instanceof Number && ((Number) rightSimplified).value == 1) {
            return leftSimplified;
        }

        if (leftSimplified == left && rightSimplified == right) {
            return this;
        }

        return new Mul(leftSimplified, rightSimplified);
    }

    /**
     * Создает новую операцию умножения.
     *
     * @param left Левое выражение.
     * @param right Правое выражение.
     * @throws ExpressionParseException Если left или right равны null.
     */
    public Mul(Expression left, Expression right) {
        if (left == null || right == null) {
            throw new ExpressionParseException("Аргументы операции умножения не могут быть null.");
        }
        this.left = left;
        this.right = right;
    }

    /**
     * Возвращает строковое представление умножения.
     *
     * @return Строка в формате "(left * right)".
     */
    @Override
    public String print() {
        return "(" + left.print() + "*" + right.print() + ")";
    }

    /**
     * Вычисляет производную произведения.
     * Использует правило производной произведения: (uv)' = u'v + uv'.
     *
     * @param var Имя переменной, по которой вычисляется производная.
     * @return Новое выражение Add с результатом применения правила произведения.
     */
    @Override
    public Expression derivative(String var) {
        return new Add(
                new Mul(left.derivative(var), right),
                new Mul(left, right.derivative(var))
        );
    }

    /**
     * Вычисляет значение произведения выражений.
     *
     * @param vars Карта значений переменных.
     * @return Произведение значений левого и правого выражений.
     */
    @Override
    public int evaluate(Map<String, Integer> vars) {
        return left.evaluate(vars) * right.evaluate(vars);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Mul other = (Mul) obj;
        return left.equals(other.left) && right.equals(other.right);
    }

    @Override
    public int hashCode() {
        return 31 * left.hashCode() + right.hashCode();
    }
}