package ru.nsu.dizmestev;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Тесты для последовательной реализации поиска.
 */
public class SequentialFinderTest {

    @Test
    public void testHasNonPrimeTrue() throws Exception {
        int[] numbers = {6, 8, 7, 13, 5, 9, 4};
        NonPrimeFinder finder = new SequentialFinder();
        assertTrue(finder.hasNonPrime(numbers));
    }

    @Test
    public void testHasNonPrimeFalse() throws Exception {
        int[] numbers = {20319251, 6997901, 6997927};
        NonPrimeFinder finder = new SequentialFinder();
        assertFalse(finder.hasNonPrime(numbers));
    }

    @Test
    public void testHasNonPrimeThrowsException() throws Exception {
        NonPrimeFinder finder = new SequentialFinder();
        assertThrows(TaskExecutionException.class, () -> finder.hasNonPrime(null));
    }
}
