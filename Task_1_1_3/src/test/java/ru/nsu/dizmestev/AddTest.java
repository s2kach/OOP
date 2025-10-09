package ru.nsu.dizmestev;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

class AddTest {

    @Test
    void testPrint() {
        Add add = new Add(new Number(3), new Number(5));
        assertEquals("(3+5)", add.print());
    }

    @Test
    void testPrintWithVariables() {
        Add add = new Add(new Variable("x"), new Variable("y"));
        assertEquals("(x+y)", add.print());
    }

    @Test
    void testCreateWithNullArgumentsThrowsException() {
        assertThrows(ExpressionParseException.class, () -> {
            new Add(null, new Number(1));
        });

        assertThrows(ExpressionParseException.class, () -> {
            new Add(new Number(1), null);
        });
    }

    @Test
    void testDerivative() {
        Add add = new Add(new Variable("x"), new Number(5));
        Expression derivative = add.derivative("x");
        assertEquals("(1+0)", derivative.print());
    }

    @Test
    void testEvaluate() {
        Add add = new Add(new Number(10), new Number(15));
        Map<String, Integer> vars = new HashMap<>();
        int result = add.evaluate(vars);
        assertEquals(25, result);
    }

    @Test
    void testEvaluateWithVariables() {
        Add add = new Add(new Variable("a"), new Variable("b"));
        Map<String, Integer> vars = new HashMap<>();
        vars.put("a", 7);
        vars.put("b", 8);
        int result = add.evaluate(vars);
        assertEquals(15, result);
    }
}