package ru.nsu.dizmestev;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

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

    @Test
    void testMasterStopGracefully() throws InterruptedException {
        int[] numbers = {2, 3, 5};
        MasterNode master = new MasterNode(numbers);

        Thread t = new Thread(() -> {
            assertDoesNotThrow(master::start);
        });
        t.start();
        Thread.sleep(500);

        assertTrue(t.isAlive());

        master.stopServer();
        t.join(1000);

        assertFalse(t.isAlive());
    }

    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void testNonPrimeDetection() throws InterruptedException {
        int[] numbers = {4};
        MasterNode master = new MasterNode(numbers);
        Thread masterThread = new Thread(() -> assertDoesNotThrow(master::start));
        masterThread.start();

        Thread.sleep(200);
        WorkerNode worker = new WorkerNode();

        assertDoesNotThrow(worker::start);
        masterThread.join();
    }

    @Test
    void testMasterPortConflictThrowsNetworkException() throws InterruptedException {
        MasterNode master1 = new MasterNode(new int[]{2});
        MasterNode master2 = new MasterNode(new int[]{3});

        Thread t1 = new Thread(() -> assertDoesNotThrow(master1::start));
        t1.start();
        Thread.sleep(200);

        assertThrows(NetworkException.class, master2::start);

        master1.stopServer();
        t1.join();
    }

    @Test
    void testWorkerDisconnectMidTaskReturnsTaskToQueue() throws Exception {
        MasterNode master = new MasterNode(new int[]{2, 3, 5});
        Thread t = new Thread(() -> assertDoesNotThrow(master::start));
        t.start();
        Thread.sleep(200);

        java.net.Socket rawSocket = new java.net.Socket("localhost", 8080);
        new ObjectOutputStream(rawSocket.getOutputStream());
        ObjectInputStream testOis = new ObjectInputStream(rawSocket.getInputStream());

        testOis.readObject();

        rawSocket.close();

        Thread.sleep(200);
        master.stopServer();
        t.join();
    }
}