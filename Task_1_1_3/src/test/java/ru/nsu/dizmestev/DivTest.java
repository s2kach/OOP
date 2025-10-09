package ru.nsu.dizmestev;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

class DivTest {

    @Test
    void testPrint() {
        Div div = new Div(new Number(10), new Number(2));
        assertEquals("(10/2)", div.print());
    }

    @Test
    void testPrintWithVariables() {
        Div div = new Div(new Variable("x"), new Variable("y"));
        assertEquals("(x/y)", div.print());
    }

    @Test
    void testCreateWithNullArgumentsThrowsException() {
        assertThrows(ExpressionParseException.class, () -> {
            new Div(null, new Number(1));
        });
    }

    @Test
    void testDerivative() {
        Div div = new Div(new Variable("x"), new Number(2));
        Expression derivative = div.derivative("x");
        assertEquals("(((1*2)-(x*0))/(2*2))", derivative.print());
    }

    @Test
    void testEvaluate() {
        Div div = new Div(new Number(15), new Number(3));
        Map<String, Integer> vars = new HashMap<>();
        int result = div.evaluate(vars);
        assertEquals(5, result);
    }

    @Test
    void testEvaluateWithVariables() {
        Div div = new Div(new Variable("x"), new Variable("y"));
        Map<String, Integer> vars = new HashMap<>();
        vars.put("x", 20);
        vars.put("y", 5);
        int result = div.evaluate(vars);
        assertEquals(4, result);
    }

    @Test
    void testEvaluateDivisionByZeroThrowsException() {
        Div div = new Div(new Number(10), new Number(0));
        Map<String, Integer> vars = new HashMap<>();
        assertThrows(ExpressionEvaluateException.class, () -> {
            div.evaluate(vars);
        });
    }

    @Test
    void testEvaluateIntegerDivision() {
        Div div = new Div(new Number(7), new Number(2));
        Map<String, Integer> vars = new HashMap<>();
        int result = div.evaluate(vars);
        assertEquals(3, result); // 7 / 2 = 3 (integer division)
    }
}