package ru.nsu.dizmestev;

import java.util.Arrays;

/**
 * Реализация поиска с использованием ParallelStream.
 */
public class StreamFinder implements NonPrimeFinder {

    /**
     * Использует параллельные стримы для поиска.
     *
     * @param numbers Массив целых чисел.
     * @return Результат проверки.
     * @throws TaskExecutionException При возникновении ошибки стрима.
     */
    @Override
    public boolean hasNonPrime(int[] numbers) throws TaskExecutionException {
        try {
            return Arrays.stream(numbers)
                    .parallel()
                    .anyMatch(n -> !PrimeChecker.isPrime(n));
        } catch (Exception e) {
            throw new TaskExecutionException("Ошибка при работе с ParallelStream.", e);
        }
    }
}
