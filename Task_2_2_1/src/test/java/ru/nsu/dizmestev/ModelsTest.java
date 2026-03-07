package ru.nsu.dizmestev;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

/**
 * Тесты для проверки моделей данных (заказы и статусы).
 */
public class ModelsTest {

    @Test
    public void testOrderCreation() {
        Order order = new Order(101);
        assertEquals(101, order.getId());
    }

    @Test
    public void testOrderStateChange() {
        Order order = new Order(202);
        order.setState(OrderState.COOKING);
        assertNotNull(order);

        order.setState(OrderState.DELIVERED);
        assertEquals(202, order.getId());
    }

    @Test
    public void testOrderStateEnum() {
        assertEquals(5, OrderState.values().length);
        assertEquals(OrderState.PENDING, OrderState.valueOf("PENDING"));
    }
}
