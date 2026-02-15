package ru.nsu.dizmestev;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Реализация поиска с использованием классических потоков Thread.
 */
public class ThreadedFinder implements NonPrimeFinder {

    private final int threadCount;

    /**
     * Конструктор с указанием количества потоков.
     *
     * @param threadCount Количество потоков.
     */
    public ThreadedFinder(int threadCount) {
        this.threadCount = threadCount;
    }

    /**
     * Разделяет массив на части и проверяет их в разных потоках.
     *
     * @param numbers Массив целых чисел.
     * @return Результат проверки.
     * @throws TaskExecutionException Если поток был прерван или возникла ошибка.
     */
    @Override
    public boolean hasNonPrime(int[] numbers) throws TaskExecutionException {
        AtomicBoolean foundNonPrime = new AtomicBoolean(false);
        List<Thread> threads = new ArrayList<>();

        try {
            int chunkSize = (numbers.length + threadCount - 1) / threadCount;

            for (int i = 0; i < threadCount; i++) {
                final int start = i * chunkSize;
                final int end = Math.min(start + chunkSize, numbers.length);

                if (start >= numbers.length) {
                    break;
                }

                Thread thread = new Thread(() -> {
                    for (int j = start; j < end; j++) {
                        if (foundNonPrime.get()) {
                            return;
                        }
                        if (!PrimeChecker.isPrime(numbers[j])) {
                            foundNonPrime.set(true);
                            return;
                        }
                    }
                });
                threads.add(thread);
                thread.start();
            }

            for (Thread thread : threads) {
                thread.join();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new TaskExecutionException("Выполнение потоков было прервано.", e);
        } catch (Exception e) {
            throw new TaskExecutionException("Ошибка многопоточного выполнения.", e);
        }

        return foundNonPrime.get();
    }
}
