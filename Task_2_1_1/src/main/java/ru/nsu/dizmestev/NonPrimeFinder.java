package ru.nsu.dizmestev;

/**
 * Интерфейс для поиска хотя бы одного составного числа в массиве.
 */
public interface NonPrimeFinder {

    /**
     * Проверяет наличие хотя бы одного непростого числа.
     *
     * @param numbers Массив целых чисел.
     * @return true, если найдено хотя бы одно составное число.
     * @throws TaskExecutionException Если произошла ошибка при вычислениях.
     */
    boolean hasNonPrime(int[] numbers) throws TaskExecutionException;
}
