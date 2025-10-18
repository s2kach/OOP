package ru.nsu.dizmestev;

import java.util.Map;

/**
 * Класс для представления переменных в выражениях.
 */
public class Variable extends Expression {
    private final String name;

    /**
     * Создает новую переменную.
     *
     * @param name Имя переменной.
     * @throws ExpressionParseException Если имя переменной не подходит.
     */
    public Variable(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new ExpressionParseException("Имя переменной не может быть пустым.");
        }

        String trimmedName = name.trim();
        for (int i = 0; i < trimmedName.length(); i++) {
            char c = trimmedName.charAt(i);
            if (!Character.isLetter(c)) {
                throw new ExpressionParseException("Имя переменной может содержать только буквы: "
                        + name);
            }
        }

        this.name = trimmedName;
    }

    /**
     * Возвращает имя переменной.
     *
     * @return Имя переменной.
     */
    @Override
    public String print() {
        return name;
    }

    /**
     * Вычисляет производную переменной.
     * Производная равна 1 если переменная совпадает с заданной, иначе 0.
     *
     * @param var Имя переменной, по которой вычисляется производная.
     * @return Новое выражение (Number(1) или Number(0)).
     */
    @Override
    public Expression derivative(String var) {
        return new Number(name.equals(var) ? 1 : 0);
    }

    /**
     * Возвращает значение переменной.
     *
     * @param vars Карта значений переменных.
     * @return Значение переменной из карты.
     * @throws ExpressionEvaluateException Если переменная не найдена в карте значений.
     */
    @Override
    public int evaluate(Map<String, Integer> vars) {
        if (!vars.containsKey(name)) {
            throw new ExpressionEvaluateException("Переменная '"
                    + name + "' не найдена в заданных значениях.");
        }
        return vars.get(name);
    }
}