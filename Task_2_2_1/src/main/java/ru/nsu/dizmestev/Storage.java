package ru.nsu.dizmestev;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Склад готовых пицц с ограниченной вместимостью.
 */
public class Storage {
    private final int capacity;
    private final List<Order> pizzas = new LinkedList<>();
    private boolean bakersWorking = true;

    /**
     * Конструктор склада с заданной максимальной вместимостью.
     *
     * @param capacity максимальное количество пицц на складе
     */
    public Storage(int capacity) {
        this.capacity = capacity;
    }

    /**
     * Помещает готовую пиццу на склад, ожидая освобождения места при заполнении.
     *
     * @param order готовый заказ
     * @throws PizzeriaInterruptedException если поток ожидания места был прерван
     */
    public synchronized void putPizza(Order order) throws PizzeriaInterruptedException {
        try {
            while (pizzas.size() >= capacity) {
                wait();
            }
            pizzas.add(order);
            notifyAll();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new PizzeriaInterruptedException("Поток добавления на склад прерван.", e);
        }
    }

    /**
     * Забирает со склада готовые пиццы в пределах вместимости багажника курьера.
     *
     * @param maxCapacity максимальное количество пицц, которое может взять курьер
     * @return список взятых пицц или null, если склад пуст и пекари закончили работу
     * @throws PizzeriaInterruptedException если поток ожидания пицц был прерван
     */
    public synchronized List<Order> takePizzas(int maxCapacity)
            throws PizzeriaInterruptedException {
        try {
            while (pizzas.isEmpty() && bakersWorking) {
                wait();
            }
            if (pizzas.isEmpty() && !bakersWorking) {
                return null;
            }
            List<Order> taken = new ArrayList<>();
            int amountToTake = Math.min(maxCapacity, pizzas.size());
            for (int i = 0; i < amountToTake; i++) {
                taken.add(pizzas.remove(0));
            }
            notifyAll();
            return taken;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new PizzeriaInterruptedException("Поток забора пицц со склада прерван.", e);
        }
    }

    /**
     * Сообщает складу, что все пекари завершили свою работу.
     */
    public synchronized void setBakersFinished() {
        bakersWorking = false;
        notifyAll();
    }
}
