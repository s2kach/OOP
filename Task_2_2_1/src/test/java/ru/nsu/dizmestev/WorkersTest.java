package ru.nsu.dizmestev;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Тесты для проверки логики пекарей и курьеров.
 */
public class WorkersTest {

    @Test
    public void testBakerLogic() throws PizzeriaException {
        OrderQueue queue = new OrderQueue();
        Storage storage = new Storage(5);
        queue.addOrder(new Order(1));
        queue.close(); // Чтобы пекарь завершил работу после 1 заказа

        Baker baker = new Baker(50, queue, storage);
        Thread bakerThread = new Thread(baker);
        bakerThread.start();

        try {
            bakerThread.join(2000);
            List<Order> pizzas = storage.takePizzas(1);
            assertEquals(1, pizzas.size());
            assertFalse(bakerThread.isAlive());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new PizzeriaInterruptedException("Тест прерван", e);
        }
    }

    @Test
    public void testCourierLogic() throws PizzeriaException {
        Storage storage = new Storage(5);
        storage.putPizza(new Order(2));
        storage.setBakersFinished(); // Чтобы курьер завершил работу

        Courier courier = new Courier(2, storage);
        Thread courierThread = new Thread(courier);
        courierThread.start();

        try {
            courierThread.join(2000);
            assertFalse(courierThread.isAlive());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new PizzeriaInterruptedException("Тест прерван", e);
        }
    }
}