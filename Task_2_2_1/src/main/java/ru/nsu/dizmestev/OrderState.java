package ru.nsu.dizmestev;

/**
 * Перечисление возможных состояний заказа в производственном процессе.
 */
public enum OrderState {
    PENDING,
    COOKING,
    READY_FOR_DELIVERY,
    DELIVERING,
    DELIVERED
}