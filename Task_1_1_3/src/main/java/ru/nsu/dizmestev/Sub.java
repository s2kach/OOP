package ru.nsu.dizmestev;

import java.util.Map;

/**
 * Класс для представления операции вычитания двух выражений.
 */
public class Sub extends Expression {
    public final Expression left;
    public final Expression right;

    @Override
    public Expression simplify() {
        Expression leftSimplified = left.simplify();
        Expression rightSimplified = right.simplify();

        if (leftSimplified instanceof Number && rightSimplified instanceof Number) {
            int leftValue = ((Number) leftSimplified).value;
            int rightValue = ((Number) rightSimplified).value;
            return new Number(leftValue - rightValue);
        }

        if (rightSimplified instanceof Number && ((Number) rightSimplified).value == 0) {
            return leftSimplified;
        }

        if (leftSimplified.equals(rightSimplified)) {
            return new Number(0);
        }

        if (leftSimplified == left && rightSimplified == right) {
            return this;
        }

        return new Sub(leftSimplified, rightSimplified);
    }

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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Sub other = (Sub) obj;
        return left.equals(other.left) && right.equals(other.right);
    }

    @Override
    public int hashCode() {
        return 31 * left.hashCode() + right.hashCode();
    }
}