package ru.nsu.dizmestev;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import java.util.concurrent.TimeUnit;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/**
 * Интеграционный тест распределенной системы.
 */
class SystemTest {

    @Test
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testMasterWorkerIntegration() throws InterruptedException {
        int[] numbers = {2, 3, 5, 7, 11, 13};
        MasterNode master = new MasterNode(numbers);

        Thread masterThread = new Thread(() -> {
            assertDoesNotThrow(master::start);
        });
        masterThread.start();

        Thread.sleep(500);

        WorkerNode worker = new WorkerNode();
        assertDoesNotThrow(worker::start);

        masterThread.join();
    }

    @Test
    @Timeout(value = 15, unit = TimeUnit.SECONDS)
    void testMultipleWorkersWithLargeArray() throws InterruptedException {
        int[] largePrimeArray = new int[100];
        java.util.Arrays.fill(largePrimeArray, 1000000007);

        MasterNode master = new MasterNode(largePrimeArray);

        Thread masterThread = new Thread(() -> {
            assertDoesNotThrow(master::start);
        });
        masterThread.start();

        Thread.sleep(500);

        int workerCount = 3;
        Thread[] workerThreads = new Thread[workerCount];

        for (int i = 0; i < workerCount; i++) {
            workerThreads[i] = new Thread(() -> {
                WorkerNode worker = new WorkerNode();
                assertDoesNotThrow(worker::start);
            });
            workerThreads[i].start();
        }

        masterThread.join();
        for (int i = 0; i < workerCount; i++) {
            workerThreads[i].join();
        }
    }
}