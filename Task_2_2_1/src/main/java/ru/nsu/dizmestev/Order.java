package ru.nsu.dizmestev;

/**
 * Класс, представляющий заказ на пиццу.
 */
public class Order {
    private final int id;
    private OrderState state;

    /**
     * Конструктор для создания нового заказа со статусом ожидания.
     *
     * @param id уникальный идентификатор заказа
     */
    public Order(int id) {
        this.id = id;
        this.state = OrderState.PENDING;
        printState();
    }

    /**
     * Возвращает уникальный номер заказа.
     *
     * @return идентификатор заказа
     */
    public int getId() {
        return id;
    }

    /**
     * Обновляет состояние заказа и выводит информацию в консоль.
     *
     * @param newState новое состояние заказа
     */
    public void setState(OrderState newState) {
        this.state = newState;
        printState();
    }

    /**
     * Внутренний метод для вывода текущего состояния заказа на экран.
     */
    private void printState() {
        System.out.println("[" + id + "] [" + state + "]");
    }
}