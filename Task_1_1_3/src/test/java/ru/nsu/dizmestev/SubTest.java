package ru.nsu.dizmestev;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

class SubTest {

    @Test
    void testPrint() {
        Sub sub = new Sub(new Number(8), new Number(3));
        assertEquals("(8-3)", sub.print());
    }

    @Test
    void testPrintWithVariables() {
        Sub sub = new Sub(new Variable("x"), new Variable("y"));
        assertEquals("(x-y)", sub.print());
    }

    @Test
    void testCreateWithNullArgumentsThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Sub(null, new Number(1));
        });
    }

    @Test
    void testDerivative() {
        Sub sub = new Sub(new Variable("x"), new Number(5));
        Expression derivative = sub.derivative("x");
        assertEquals("(1-0)", derivative.print());
    }

    @Test
    void testEvaluate() {
        Sub sub = new Sub(new Number(20), new Number(7));
        Map<String, Integer> vars = new HashMap<>();
        int result = sub.evaluate(vars);
        assertEquals(13, result);
    }

    @Test
    void testEvaluateWithVariables() {
        Sub sub = new Sub(new Variable("x"), new Variable("y"));
        Map<String, Integer> vars = new HashMap<>();
        vars.put("x", 15);
        vars.put("y", 4);
        int result = sub.evaluate(vars);
        assertEquals(11, result);
    }

    @Test
    void testEvaluateNegativeResult() {
        Sub sub = new Sub(new Number(5), new Number(10));
        Map<String, Integer> vars = new HashMap<>();
        int result = sub.evaluate(vars);
        assertEquals(-5, result);
    }
}