package ru.nsu.dizmestev;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

class NumberTest {

    @Test
    void testPrint() {
        Number number = new Number(42);
        assertEquals("42", number.print());
    }

    @Test
    void testPrintZero() {
        Number number = new Number(0);
        assertEquals("0", number.print());
    }

    @Test
    void testPrintNegative() {
        Number number = new Number(-15);
        assertEquals("-15", number.print());
    }

    @Test
    void testDerivative() {
        Number number = new Number(10);
        Expression derivative = number.derivative("");
        assertEquals("0", derivative.print());
    }

    @Test
    void testEvaluate() {
        Number number = new Number(7);
        Map<String, Integer> vars = new HashMap<>();
        vars.put("x", 5);
        int result = number.evaluate(vars);
        assertEquals(7, result);
    }

    @Test
    void testEvaluateWithEmptyVars() {
        Number number = new Number(3);
        int result = number.evaluate(new HashMap<>());
        assertEquals(3, result);
    }
}