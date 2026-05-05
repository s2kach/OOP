package ru.nsu.dizmestev;

import java.util.List;

/**
 * Курьер, который забирает готовые пиццы со склада и доставляет их.
 */
public class Courier implements Runnable {
    private final int trunkCapacity;
    private final Storage storage;

    /**
     * Конструктор курьера.
     *
     * @param trunkCapacity вместимость багажника
     * @param storage склад готовой продукции
     */
    public Courier(int trunkCapacity, Storage storage) {
        this.trunkCapacity = trunkCapacity;
        this.storage = storage;
    }

    /**
     * Основной цикл работы курьера.
     */
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            List<Order> orders = null;
            try {
                orders = storage.takePizzas(trunkCapacity);
                if (orders == null) {
                    break;
                }
                for (Order order : orders) {
                    order.setState(OrderState.DELIVERING);
                }
                Thread.sleep(1000L * orders.size());
                for (Order order : orders) {
                    order.setState(OrderState.DELIVERED);
                }

            } catch (PizzeriaInterruptedException e) {
                System.err.println("Ошибка в работе очередей у курьера: " + e.getMessage());
            } catch (InterruptedException e) {
                if (orders != null && !orders.isEmpty()) {
                    for (Order order : orders) {
                        order.setState(OrderState.READY_FOR_DELIVERY);
                    }
                    storage.returnPizzas(orders);
                }
                System.err.println("Поток курьера был принудительно прерван во время доставки.");
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
