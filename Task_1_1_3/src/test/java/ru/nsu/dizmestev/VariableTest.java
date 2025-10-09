package ru.nsu.dizmestev;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

class VariableTest {

    @Test
    void testPrint() {
        Variable variable = new Variable("x");
        assertEquals("x", variable.print());
    }

    @Test
    void testPrintMultiLetter() {
        Variable variable = new Variable("alpha");
        assertEquals("alpha", variable.print());
    }

    @Test
    void testCreateVariableWithEmptyNameThrowsException() {
        assertThrows(ExpressionParseException.class, () -> {
            new Variable("");
        });
    }

    @Test
    void testCreateVariableWithNullNameThrowsException() {
        assertThrows(ExpressionParseException.class, () -> {
            new Variable(null);
        });
    }

    @Test
    void testCreateVariableWithInvalidNameThrowsException() {
        assertThrows(ExpressionParseException.class, () -> {
            new Variable("x1");
        });
    }

    @Test
    void testDerivativeSameVariable() {
        Variable variable = new Variable("x");
        Expression derivative = variable.derivative("x");
        assertEquals("1", derivative.print());
    }

    @Test
    void testDerivativeDifferentVariable() {
        Variable variable = new Variable("y");
        Expression derivative = variable.derivative("x");
        assertEquals("0", derivative.print());
    }

    @Test
    void testEvaluate() {
        Variable variable = new Variable("x");
        Map<String, Integer> vars = new HashMap<>();
        vars.put("x", 10);
        int result = variable.evaluate(vars);
        assertEquals(10, result);
    }

    @Test
    void testEvaluateMultiLetters() {
        Variable variable = new Variable("alpha");
        Map<String, Integer> vars = new HashMap<>();
        vars.put("alpha", 10);
        int result = variable.evaluate(vars);
        assertEquals(10, result);
    }

    @Test
    void testEvaluateMissingVariableThrowsException() {
        Variable variable = new Variable("z");
        Map<String, Integer> vars = new HashMap<>();
        vars.put("x", 10);
        vars.put("y", 20);
        assertThrows(ExpressionEvaluateException.class, () -> {
            variable.evaluate(vars);
        });
    }
}