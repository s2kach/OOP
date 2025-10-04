package ru.nsu.dizmestev;

import java.util.HashMap;
import java.util.Map;

/**
 * Абстрактный класс для представления математических выражений.
 * Является базовым классом для всех типов выражений.
 */
public abstract class Expression {

    /**
     * Возвращает строковое представление выражения.
     *
     * @return Строковое представление выражения в формате со скобками.
     */
    public abstract String print();

    /**
     * Вычисляет производную выражения по заданной переменной.
     *
     * @param var Имя переменной, по которой вычисляется производная.
     * @return Новое выражение, представляющее производную.
     */
    public abstract Expression derivative(String var);

    /**
     * Вычисляет значение выражения при заданных значениях переменных.
     *
     * @param vars Карта значений переменных.
     * @return Результат вычисления выражения.
     */
    public abstract int evaluate(Map<String, Integer> vars);

    /**
     * Вычисляет значение выражения при заданных значениях переменных.
     *
     * @param varsStr Строка с значениями переменных в формате "var1 = value1; var2 = value2".
     * @return Результат вычисления выражения.
     * @throws IllegalArgumentException Если строка с переменными имеет неверный формат.
     */
    public int eval(String varsStr) {
        if (varsStr == null || varsStr.trim().isEmpty()) {
            throw new IllegalArgumentException("Строка с переменными не может быть пустой.");
        }
        Map<String, Integer> vars = parseVariables(varsStr);
        return evaluate(vars);
    }

    /**
     * Парсит строку со значениями переменных в карту.
     *
     * @param varsStr Строка со значениями переменных.
     * @return Карта с именами переменных и их значениями.
     * @throws IllegalArgumentException Если строка имеет неверный формат.
     */
    private Map<String, Integer> parseVariables(String varsStr) {
        Map<String, Integer> vars = new HashMap<>();
        String[] assignments = varsStr.split(";");

        for (String assignment : assignments) {
            String trimmedAssignment = assignment.trim();
            if (trimmedAssignment.isEmpty()) {
                continue;
            }

            String[] parts = trimmedAssignment.split("=");
            if (parts.length != 2) {
                throw new IllegalArgumentException("Неверный формат присваивания: " + assignment
                        + ". Ожидается формат: variable = value");
            }

            String varName = parts[0].trim();
            String valueStr = parts[1].trim();

            if (varName.isEmpty()) {
                throw new IllegalArgumentException("Имя переменной не может быть"
                        + " пустым в присваивании: " + assignment);
            }

            try {
                int value = Integer.parseInt(valueStr);
                vars.put(varName, value);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Неверный числовой формат в присваивании: "
                        + assignment + ". Значение должно быть целым числом.", e);
            }
        }

        if (vars.isEmpty()) {
            throw new IllegalArgumentException("Не найдено корректных присваиваний переменных.");
        }

        return vars;
    }

    /**
     * Парсит математическое выражение из строки.
     *
     * @param expressionStr Строка с математическим выражением.
     * @return Распарсенное выражение.
     * @throws IllegalArgumentException Если строка выражения имеет неверный формат.
     */
    public static Expression parse(String expressionStr) {
        if (expressionStr == null || expressionStr.trim().isEmpty()) {
            throw new IllegalArgumentException("Строка выражения не может быть пустой.");
        }

        String trimmed = expressionStr.trim();
        return parseExpression(trimmed);
    }

    /**
     * Рекурсивно парсит математическое выражение.
     *
     * @param str Строка для парсинга.
     * @return Распарсенное выражение.
     * @throws IllegalArgumentException Если выражение имеет неверный формат.
     */
    private static Expression parseExpression(String str) {
        if (str.startsWith("(") && str.endsWith(")")) {
            String content = str.substring(1, str.length() - 1).trim();
            return parseComplexExpression(content);
        } else {
            return parseSimpleExpression(str);
        }
    }

    /**
     * Парсит сложное выражение (в скобках).
     *
     * @param content Содержимое внутри скобок.
     * @return Распарсенное выражение.
     * @throws IllegalArgumentException Если выражение имеет неверный формат.
     */
    private static Expression parseComplexExpression(String content) {
        int operatorIndex = findOperatorIndex(content);
        if (operatorIndex == -1) {
            throw new IllegalArgumentException("Не найден оператор в выражении: "
                    + content);
        }

        char operator = content.charAt(operatorIndex);
        String leftStr = content.substring(0, operatorIndex).trim();
        String rightStr = content.substring(operatorIndex + 1).trim();

        if (leftStr.isEmpty() || rightStr.isEmpty()) {
            throw new IllegalArgumentException("Неполное выражение для оператора "
                    + operator + ": " + content);
        }

        Expression left = parseExpression(leftStr);
        Expression right = parseExpression(rightStr);

        switch (operator) {
            case '+': return new Add(left, right);
            case '-': return new Sub(left, right);
            case '*': return new Mul(left, right);
            case '/': return new Div(left, right);
            default: throw new IllegalArgumentException("Неизвестный оператор: "
                    + operator);
        }
    }

    /**
     * Находит индекс оператора в выражении.
     *
     * @param str Строка для поиска.
     * @return Индекс оператора или -1 если не найден.
     */
    private static int findOperatorIndex(String str) {
        int bracketCount = 0;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == '(') {
                bracketCount++;
            } else if (c == ')') {
                bracketCount--;
            } else if (bracketCount == 0 && (c == '+' || c == '-' || c == '*' || c == '/')) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Парсит простое выражение (число или переменную).
     *
     * @param str Строка для парсинга.
     * @return Распарсенное выражение.
     * @throws IllegalArgumentException Если выражение имеет неверный формат.
     */
    private static Expression parseSimpleExpression(String str) {
        try {
            int value = Integer.parseInt(str);
            return new Number(value);
        } catch (NumberFormatException e) {
            if (isValidVariableName(str)) {
                return new Variable(str);
            } else {
                throw new IllegalArgumentException("Неверный формат выражения: "
                        + str + ". Ожидается число или переменная.");
            }
        }
    }

    /**
     * Проверяет корректность имени переменной.
     *
     * @param name Имя переменной для проверки.
     * @return true если имя корректно, false в противном случае.
     */
    private static boolean isValidVariableName(String name) {
        if (name == null || name.isEmpty()) {
            return false;
        }

        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if (!Character.isLetter(c)) {
                return false;
            }
        }
        return true;
    }
}