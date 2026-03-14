package ru.nsu.dizmestev;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Тесты для проверки потокобезопасных структур (очереди заказов и склада).
 */
public class QueuesTest {

    @Test
    public void testOrderQueueAddAndTake() throws PizzeriaInterruptedException {
        OrderQueue queue = new OrderQueue();
        Order order = new Order(1);
        queue.addOrder(order);

        Order taken = queue.takeOrder();
        assertEquals(1, taken.getId());
    }

    @Test
    public void testOrderQueueClose() throws PizzeriaInterruptedException {
        OrderQueue queue = new OrderQueue();
        queue.close();

        Order taken = queue.takeOrder();
        assertNull(taken);
    }

    @Test
    public void testStoragePutAndTake() throws PizzeriaInterruptedException {
        Storage storage = new Storage(2);
        storage.putPizza(new Order(10));
        storage.putPizza(new Order(11));

        List<Order> taken = storage.takePizzas(3);
        assertEquals(2, taken.size());
        assertEquals(10, taken.get(0).getId());
    }

    @Test
    public void testStorageBakersFinished() throws PizzeriaInterruptedException {
        Storage storage = new Storage(5);
        storage.setBakersFinished();

        List<Order> taken = storage.takePizzas(2);
        assertNull(taken);
    }

    @Test
    public void testOrderQueueInterruption() {
        OrderQueue queue = new OrderQueue();
        Thread.currentThread().interrupt();

        assertThrows(PizzeriaInterruptedException.class, queue::takeOrder);
    }

    @Test
    public void testReturnOrderToQueue() throws PizzeriaInterruptedException {
        OrderQueue queue = new OrderQueue();
        queue.addOrder(new Order(1));

        queue.returnOrder(new Order(2));

        assertEquals(2, queue.takeOrder().getId());
        assertEquals(1, queue.takeOrder().getId());
    }

    @Test
    public void testReturnPizzasToStorage() throws PizzeriaInterruptedException {
        Storage storage = new Storage(5);
        storage.putPizza(new Order(10));

        List<Order> returned = List.of(new Order(20), new Order(21));
        storage.returnPizzas(returned);

        List<Order> taken = storage.takePizzas(3);
        assertEquals(3, taken.size());
        assertEquals(20, taken.get(0).getId());
        assertEquals(21, taken.get(1).getId());
        assertEquals(10, taken.get(2).getId());
    }
}
