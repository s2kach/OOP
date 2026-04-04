package ru.nsu.dizmestev;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.Arrays;
import org.junit.jupiter.api.Test;

/**
 * Класс для проведения сравнительного тестирования производительности различных реализаций.
 */
public class BenchmarkTest {

    @Test
    public void runPerformanceBenchmark() throws TaskExecutionException {
        int count = 500000;
        int[] data = generateLargePrimeArray(count);

        System.out.println("Начало тестирования на массиве из " + count + " элементов.");
        System.out.println("---------------------------------------------------------");

        NonPrimeFinder sequential = new SequentialFinder();
        long startTime = System.currentTimeMillis();
        boolean resultSeq = sequential.hasNonPrime(data);
        long endTime = System.currentTimeMillis();
        assertFalse(resultSeq);
        System.out.println("1. Последовательное выполнение: " + (endTime - startTime) + " мс");

        for (int threads = 2; threads <= 8; threads++) {
            NonPrimeFinder threaded = new ThreadedFinder(threads);
            startTime = System.currentTimeMillis();
            boolean resultThread = threaded.hasNonPrime(data);
            endTime = System.currentTimeMillis();
            assertFalse(resultThread);
            System.out.println("2." + (threads - 1) + ". Параллельно (" + threads + " потоков): "
                    + (endTime - startTime) + " мс");
        }

        NonPrimeFinder streamFinder = new StreamFinder();
        startTime = System.currentTimeMillis();
        boolean resultStream = streamFinder.hasNonPrime(data);
        endTime = System.currentTimeMillis();
        assertFalse(resultStream);
        System.out.println("3. ParallelStream: " + (endTime - startTime) + " мс");
        System.out.println("---------------------------------------------------------");
    }

    private int[] generateLargePrimeArray(int size) throws TaskExecutionException {
        try {
            int[] array = new int[size];
            Arrays.fill(array, 20319251);
            return array;
        } catch (Exception e) {
            throw new TaskExecutionException("Не удалось сгенерировать тестовые данные.", e);
        }
    }
}
