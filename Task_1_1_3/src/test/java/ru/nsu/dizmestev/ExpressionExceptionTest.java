package ru.nsu.dizmestev;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ExpressionExceptionTest {

    @Test
    void testExpressionParseExceptionWithMessage() {
        ExpressionParseException exception = new ExpressionParseException("Ошибка");

        assertEquals("Ошибка", exception.getMessage());
    }

    @Test
    void testExpressionParseExceptionWithMessageAndCause() {
        IllegalArgumentException cause = new IllegalArgumentException("Причина");
        ExpressionParseException exception = new ExpressionParseException("Ошибка", cause);

        assertEquals("Ошибка", exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertEquals("Причина", exception.getCause().getMessage());
    }

    @Test
    void testExpressionEvaluationExceptionWithMessage() {
        ExpressionEvaluateException exception
                = new ExpressionEvaluateException("Ошибка вычисления");
        assertEquals("Ошибка вычисления", exception.getMessage());
        assertTrue(exception instanceof ExpressionException);
    }

    @Test
    void testExpressionEvaluationExceptionWithMessageAndCause() {
        ArithmeticException cause = new ArithmeticException("Деление на ноль");
        ExpressionEvaluateException exception
                = new ExpressionEvaluateException("Ошибка вычисления", cause);

        assertEquals("Ошибка вычисления", exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertEquals("Деление на ноль", exception.getCause().getMessage());
    }
}