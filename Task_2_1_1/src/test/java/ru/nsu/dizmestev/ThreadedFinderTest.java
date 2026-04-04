package ru.nsu.dizmestev;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Тесты для многопоточной реализации поиска.
 */
public class ThreadedFinderTest {

    @Test
    public void testHasNonPrimeWithThreads() throws Exception {
        int[] numbers = {2, 3, 5, 7, 11, 12};
        NonPrimeFinder finder = new ThreadedFinder(4);
        assertTrue(finder.hasNonPrime(numbers));
    }

    @Test
    public void testAllPrimesWithThreads() throws Exception {
        int[] numbers = {2, 3, 5, 7, 11, 13, 17, 19};
        NonPrimeFinder finder = new ThreadedFinder(2);
        assertFalse(finder.hasNonPrime(numbers));
    }

    @Test
    public void testEmptyArray4Threads() throws Exception {
        int[] numbers = {};
        NonPrimeFinder finder = new ThreadedFinder(4);
        assertFalse(finder.hasNonPrime(numbers));
    }

    @Test
    public void testHasNonPrimeWith1Elem() throws Exception {
        int[] numbers = {6};
        NonPrimeFinder finder = new ThreadedFinder(4);
        assertTrue(finder.hasNonPrime(numbers));
    }

    @Test
    public void testWithNull() throws Exception {
        NonPrimeFinder finder = new ThreadedFinder(4);
        assertThrows(TaskExecutionException.class, () -> finder.hasNonPrime(null));
    }
}
