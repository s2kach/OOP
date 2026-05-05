package ru.nsu.dizmestev;

import java.util.ArrayList;
import java.util.List;

/**
 * Главный класс-симулятор работы пиццерии.
 */
public class Pizzeria {
    private final List<Thread> bakerThreads = new ArrayList<>();
    private final List<Thread> courierThreads = new ArrayList<>();
    private final OrderQueue orderQueue;
    private final Storage storage;
    private int orderIdCounter = 1;

    /**
     * Конструктор пиццерии, инициализирующий сотрудников по конфигурации.
     *
     * @param config конфигурация с настройками
     */
    public Pizzeria(Config config) {
        this.orderQueue = new OrderQueue();
        this.storage = new Storage(config.getStorageCapacity());

        for (int speed : config.getBakersSpeed()) {
            bakerThreads.add(new Thread(new Baker(speed, orderQueue, storage)));
        }

        for (int capacity : config.getCouriersCapacity()) {
            courierThreads.add(new Thread(new Courier(capacity, storage)));
        }
    }

    /**
     * Создает новый заказ и помещает его в общую очередь.
     */
    public void addOrder() {
        orderQueue.addOrder(new Order(orderIdCounter++));
    }

    /**
     * Запускает все рабочие потоки пекарей и курьеров.
     */
    public void start() {
        for (Thread bakerThread : bakerThreads) {
            bakerThread.start();
        }
        for (Thread courierThread : courierThreads) {
            courierThread.start();
        }
    }

    /**
     * Инициирует корректное завершение работы пиццерии, дожидаясь выполнения всех взятых заказов.
     *
     * @throws PizzeriaInterruptedException если процесс ожидания завершения был прерван
     */
    public void shutdown() throws PizzeriaInterruptedException {
        orderQueue.close();

        try {
            for (Thread bakerThread : bakerThreads) {
                bakerThread.join();
            }
            storage.setBakersFinished();
            for (Thread courierThread : courierThreads) {
                courierThread.join();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new PizzeriaInterruptedException("Ожидание завершения потоков"
                    + " рабочих было прервано.", e);
        }
    }
}
