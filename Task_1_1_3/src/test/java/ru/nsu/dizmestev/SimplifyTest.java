package ru.nsu.dizmestev;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class SimplifyTest {

    @Test
    void testSimplifyNumber() {
        Expression number = new Number(5);
        Expression simplified = number.simplify();
        assertEquals("5", simplified.print());
        assertTrue(simplified instanceof Number);
    }

    @Test
    void testSimplifyVariable() {
        Expression variable = new Variable("x");
        Expression simplified = variable.simplify();
        assertEquals("x", simplified.print());
        assertTrue(simplified instanceof Variable);
    }

    @Test
    void testSimplifyAddNumbers() {
        // (3 + 5) -> 8
        Expression add = new Add(new Number(3), new Number(5));
        Expression simplified = add.simplify();
        assertEquals("8", simplified.print());
        assertTrue(simplified instanceof Number);
    }

    @Test
    void testSimplifyAddWithZero() {
        // (0 + x) -> x
        Expression add = new Add(new Number(0), new Variable("x"));
        Expression simplified = add.simplify();
        assertEquals("x", simplified.print());
        assertTrue(simplified instanceof Variable);

        // (x + 0) -> x
        Expression add2 = new Add(new Variable("x"), new Number(0));
        Expression simplified2 = add2.simplify();
        assertEquals("x", simplified2.print());
        assertTrue(simplified2 instanceof Variable);
    }

    @Test
    void testSimplifySubNumbers() {
        // (8 - 3) -> 5
        Expression sub = new Sub(new Number(8), new Number(3));
        Expression simplified = sub.simplify();
        assertEquals("5", simplified.print());
        assertTrue(simplified instanceof Number);
    }

    @Test
    void testSimplifySubWithZero() {
        // (x - 0) -> x
        Expression sub = new Sub(new Variable("x"), new Number(0));
        Expression simplified = sub.simplify();
        assertEquals("x", simplified.print());
        assertTrue(simplified instanceof Variable);
    }

    @Test
    void testSimplifySubSameExpressions() {
        // (x - x) -> 0
        Expression sub = new Sub(new Variable("x"), new Variable("x"));
        Expression simplified = sub.simplify();
        assertEquals("0", simplified.print());
        assertTrue(simplified instanceof Number);
    }

    @Test
    void testSimplifyMulNumbers() {
        // (4 * 3) -> 12
        Expression mul = new Mul(new Number(4), new Number(3));
        Expression simplified = mul.simplify();
        assertEquals("12", simplified.print());
        assertTrue(simplified instanceof Number);
    }

    @Test
    void testSimplifyMulWithZero() {
        // (0 * x) -> 0
        Expression mul = new Mul(new Number(0), new Variable("x"));
        Expression simplified = mul.simplify();
        assertEquals("0", simplified.print());
        assertTrue(simplified instanceof Number);

        // (x * 0) -> 0
        Expression mul2 = new Mul(new Variable("x"), new Number(0));
        Expression simplified2 = mul2.simplify();
        assertEquals("0", simplified2.print());
        assertTrue(simplified2 instanceof Number);
    }

    @Test
    void testSimplifyMulWithOne() {
        // (1 * x) -> x
        Expression mul = new Mul(new Number(1), new Variable("x"));
        Expression simplified = mul.simplify();
        assertEquals("x", simplified.print());
        assertTrue(simplified instanceof Variable);

        // (x * 1) -> x
        Expression mul2 = new Mul(new Variable("x"), new Number(1));
        Expression simplified2 = mul2.simplify();
        assertEquals("x", simplified2.print());
        assertTrue(simplified2 instanceof Variable);
    }

    @Test
    void testSimplifyDivNumbers() {
        // (10 / 2) -> 5
        Expression div = new Div(new Number(10), new Number(2));
        Expression simplified = div.simplify();
        assertEquals("5", simplified.print());
        assertTrue(simplified instanceof Number);
    }

    @Test
    void testSimplifyDivByOne() {
        // (x / 1) -> x
        Expression div = new Div(new Variable("x"), new Number(1));
        Expression simplified = div.simplify();
        assertEquals("x", simplified.print());
        assertTrue(simplified instanceof Variable);
    }

    @Test
    void testSimplifyDivZeroByAnything() {
        // (0 / x) -> 0
        Expression div = new Div(new Number(0), new Variable("x"));
        Expression simplified = div.simplify();
        assertEquals("0", simplified.print());
        assertTrue(simplified instanceof Number);
    }

    @Test
    void testSimplifyDivSameExpressions() {
        // (x / x) -> 1
        Expression div = new Div(new Variable("x"), new Variable("x"));
        Expression simplified = div.simplify();
        assertEquals("1", simplified.print());
        assertTrue(simplified instanceof Number);
    }

    @Test
    void testSimplifyComplexExpression() {
        // ((3 + 0) * (x / 1)) -> (3 * x) -> (3*x)
        Expression complex = new Mul(
                new Add(new Number(3), new Number(0)),
                new Div(new Variable("x"), new Number(1))
        );
        Expression simplified = complex.simplify();
        assertEquals("(3*x)", simplified.print());
    }

    @Test
    void testSimplifyNestedExpression() {
        // (((2 * 0) + x) - (x * 1)) -> ((0 + x) - x) -> (x - x) -> 0
        Expression nested = new Sub(
                new Add(
                        new Mul(new Number(2), new Number(0)),
                        new Variable("x")
                ),
                new Mul(new Variable("x"), new Number(1))
        );
        Expression simplified = nested.simplify();
        assertEquals("0", simplified.print());
        assertTrue(simplified instanceof Number);
    }

    @Test
    void testSimplifyExpressionWithoutVariables() {
        // ((3+2)*(4-1)) -> (5*3) -> 15
        Expression expr = new Mul(
                new Add(new Number(3), new Number(2)),
                new Sub(new Number(4), new Number(1))
        );
        Expression simplified = expr.simplify();
        assertEquals("15", simplified.print());
        assertTrue(simplified instanceof Number);
    }

    @Test
    void testSimplifyKeepsOriginalUnchanged() {
        Expression original = new Add(new Number(0), new Variable("x"));
        Expression simplified = original.simplify();

        assertEquals("(0+x)", original.print());
        assertEquals("x", simplified.print());
    }
}