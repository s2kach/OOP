package ru.nsu.dizmestev;

/**
 * Реализация поиска непростых чисел в одном потоке.
 */
public class SequentialFinder implements NonPrimeFinder {

    /**
     * Последовательно проверяет элементы массива.
     *
     * @param numbers Массив целых чисел.
     * @return Результат проверки.
     * @throws TaskExecutionException При возникновении ошибки.
     */
    @Override
    public boolean hasNonPrime(int[] numbers) throws TaskExecutionException {
        try {
            for (int number : numbers) {
                if (!PrimeChecker.isPrime(number)) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            throw new TaskExecutionException("Ошибка при последовательном выполнении.", e);
        }
    }
}
