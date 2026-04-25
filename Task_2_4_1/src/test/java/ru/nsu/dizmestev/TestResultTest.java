package ru.nsu.dizmestev;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class TestResultTest {

    @Test
    void testToStringFormattingAllPassed() {
        TestResult result = new TestResult(10, 0, 0, 0);
        assertEquals("10/0/0", result.toString());
        assertEquals(0, result.getFailures());
        assertEquals(10, result.getTotal());
    }

    @Test
    void testToStringFormattingWithFailuresAndSkipped() {
        TestResult result = new TestResult(10, 2, 1, 1);
        assertEquals("6/3/1", result.toString());
    }

    @Test
    void testToStringFormattingEmpty() {
        TestResult result = new TestResult(0, 0, 0, 0);
        assertEquals("0/0/0", result.toString());
    }
}