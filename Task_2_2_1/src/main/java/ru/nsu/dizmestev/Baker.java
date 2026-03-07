package ru.nsu.dizmestev;

/**
 * Пекарь, который берет заказы из очереди и готовит их.
 */
public class Baker implements Runnable {
    private final int speed;
    private final OrderQueue orderQueue;
    private final Storage storage;

    /**
     * Конструктор пекаря.
     *
     * @param speed время приготовления одной пиццы в миллисекундах
     * @param orderQueue очередь входящих заказов
     * @param storage склад готовой продукции
     */
    public Baker(int speed, OrderQueue orderQueue, Storage storage) {
        this.speed = speed;
        this.orderQueue = orderQueue;
        this.storage = storage;
    }

    /**
     * Основной цикл работы пекаря.
     */
    @Override
    public void run() {
        try {
            while (true) {
                Order order = orderQueue.takeOrder();
                if (order == null) {
                    break;
                }
                order.setState(OrderState.COOKING);
                Thread.sleep(speed);
                order.setState(OrderState.READY_FOR_DELIVERY);
                storage.putPizza(order);
            }
        } catch (PizzeriaInterruptedException e) {
            System.err.println("Ошибка в работе очередей у пекаря: " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Поток пекаря был принудительно прерван во время сна.");
        }
    }
}
