package ru.nsu.dizmestev;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

class MulTest {

    @Test
    void testPrint() {
        Mul mul = new Mul(new Number(4), new Number(6));
        assertEquals("(4*6)", mul.print());
    }

    @Test
    void testPrintWithVariables() {
        Mul mul = new Mul(new Variable("a"), new Variable("b"));
        assertEquals("(a*b)", mul.print());
    }

    @Test
    void testCreateWithNullArgumentsThrowsException() {
        assertThrows(ExpressionParseException.class, () -> {
            new Mul(null, new Number(1));
        });
    }

    @Test
    void testDerivative() {
        Mul mul = new Mul(new Variable("x"), new Number(3));
        Expression derivative = mul.derivative("x");
        assertEquals("((1*3)+(x*0))", derivative.print());
    }

    @Test
    void testDerivativeComplex() {
        Mul mul = new Mul(new Variable("x"), new Variable("y"));
        Expression derivative = mul.derivative("x");
        assertEquals("((1*y)+(x*0))", derivative.print());
    }

    @Test
    void testEvaluate() {
        Mul mul = new Mul(new Number(5), new Number(7));
        Map<String, Integer> vars = new HashMap<>();
        int result = mul.evaluate(vars);
        assertEquals(35, result);
    }

    @Test
    void testEvaluateWithVariables() {
        Mul mul = new Mul(new Variable("x"), new Variable("y"));
        Map<String, Integer> vars = new HashMap<>();
        vars.put("x", 3);
        vars.put("y", 4);
        int result = mul.evaluate(vars);
        assertEquals(12, result);
    }

    @Test
    void testEvaluateWithZero() {
        Mul mul = new Mul(new Number(0), new Variable("x"));
        Map<String, Integer> vars = new HashMap<>();
        vars.put("x", 100);
        int result = mul.evaluate(vars);
        assertEquals(0, result);
    }
}