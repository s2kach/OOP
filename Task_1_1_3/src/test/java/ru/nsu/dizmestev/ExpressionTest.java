package ru.nsu.dizmestev;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class ExpressionTest {

    @Test
    void testParseValidExpression() {
        Expression expr = Expression.parse("(3+(2*x))");
        assertEquals("(3+(2*x))", expr.print());
    }

    @Test
    void testParseComplexExpression() {
        Expression expr = Expression.parse("((a+b)*(c-d))");
        assertEquals("((a+b)*(c-d))", expr.print());
    }

    @Test
    void testParseInvalidExpressionThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            Expression.parse("(3+*2)");
        });
    }

    @Test
    void testParseEmptyExpressionThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            Expression.parse("");
        });
    }

    @Test
    void testParseNullExpressionThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            Expression.parse(null);
        });
    }

    @Test
    void testEvalWithValidVariables() {
        Expression expr = Expression.parse("(x+y)");
        int result = expr.eval("x = 5; y = 3");
        assertEquals(8, result);
    }

    @Test
    void testEvalWithEmptyVariablesThrowsException() {
        Expression expr = Expression.parse("(x+y)");
        assertThrows(IllegalArgumentException.class, () -> {
            expr.eval("");
        });
    }

    @Test
    void testEvalWithInvalidFormatThrowsException() {
        Expression expr = Expression.parse("(x+y)");
        assertThrows(IllegalArgumentException.class, () -> {
            expr.eval("x=5;y=");
        });
    }
}