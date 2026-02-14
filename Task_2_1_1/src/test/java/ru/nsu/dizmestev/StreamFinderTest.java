package ru.nsu.dizmestev;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Тесты для реализации через ParallelStream.
 */
public class StreamFinderTest {

    @Test
    public void testStreamWithNonPrimes() throws Exception {
        int[] numbers = {11, 13, 17, 20};
        NonPrimeFinder finder = new StreamFinder();
        assertTrue(finder.hasNonPrime(numbers));
    }

    @Test
    public void testEmptyArray() throws Exception {
        int[] numbers = {};
        NonPrimeFinder finder = new StreamFinder();
        assertFalse(finder.hasNonPrime(numbers));
    }
}
