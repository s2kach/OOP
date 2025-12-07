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
        assertThrows(ExpressionParseException.class, () -> {
            Expression.parse("(3+*2)");
        });
    }

    @Test
    void testParseEmptyExpressionThrowsException() {
        assertThrows(ExpressionParseException.class, () -> {
            Expression.parse("");
        });
    }

    @Test
    void testParseNullExpressionThrowsException() {
        assertThrows(ExpressionParseException.class, () -> {
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
        assertThrows(ExpressionEvaluateException.class, () -> {
            expr.eval("");
        });
    }

    @Test
    void testEvalWithInvalidFormatThrowsException() {
        Expression expr = Expression.parse("(x+y)");
        assertThrows(ExpressionParseException.class, () -> {
            expr.eval("x=5;y=");
        });
    }

    @Test
    void testParseWithoutParenthesesSimple() {
        Expression expr = Expression.parseWithoutParentheses("3+2");
        assertEquals("(3+2)", expr.print());
    }

    @Test
    void testParseWithoutParenthesesWithPriority() {
        Expression expr = Expression.parseWithoutParentheses("3+2*4");
        assertEquals("(3+(2*4))", expr.print());
    }

    @Test
    void testParseWithoutParenthesesComplex() {
        Expression expr = Expression.parseWithoutParentheses("a+b*c-d/e");
        assertEquals("((a+(b*c))-(d/e))", expr.print());
    }

    @Test
    void testParseWithoutParenthesesWithVariables() {
        Expression expr = Expression.parseWithoutParentheses("x*y+10/z");
        assertEquals("((x*y)+(10/z))", expr.print());
    }

    @Test
    void testParseWithoutParenthesesWithParentheses() {
        Expression expr = Expression.parseWithoutParentheses("(a+b)*(c-d)");
        assertEquals("((a+b)*(c-d))", expr.print());
    }
}