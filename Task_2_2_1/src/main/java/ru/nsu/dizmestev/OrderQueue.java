package ru.nsu.dizmestev;

import java.util.LinkedList;
import java.util.List;

/**
 * Очередь заказов, ожидающих приготовления.
 */
public class OrderQueue {
    private final List<Order> orders = new LinkedList<>();
    private boolean isOpen = true;

    /**
     * Добавляет новый заказ в очередь и оповещает ожидающих пекарей.
     *
     * @param order заказ для добавления
     */
    public synchronized void addOrder(Order order) {
        orders.add(order);
        notifyAll();
    }

    /**
     * Берет заказ из очереди, при необходимости ожидая его появления.
     *
     * @return заказ или null, если очередь закрыта и пуста
     * @throws PizzeriaInterruptedException если поток ожидания был прерван
     */
    public synchronized Order takeOrder() throws PizzeriaInterruptedException {
        try {
            while (orders.isEmpty() && isOpen) {
                wait();
            }
            if (orders.isEmpty() && !isOpen) {
                return null;
            }
            return orders.remove(0);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new PizzeriaInterruptedException("Поток ожидания нового заказа прерван.", e);
        }
    }

    /**
     * Закрывает очередь для приема новых заказов (инициирует процесс остановки).
     */
    public synchronized void close() {
        isOpen = false;
        notifyAll();
    }

    /**
     * Экстренно возвращает заказ обратно в очередь.
     */
    public synchronized void returnOrder(Order order) {
        orders.add(0, order);
        notifyAll();
    }
}
