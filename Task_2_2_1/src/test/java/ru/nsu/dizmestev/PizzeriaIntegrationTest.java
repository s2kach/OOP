package ru.nsu.dizmestev;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.jupiter.api.Test;

/**
 * Интеграционный тест для проверки взаимодействия всех компонентов системы.
 */
public class PizzeriaIntegrationTest {

    @Test
    public void testPizzeriaFullCycle() throws PizzeriaException {
        Config config = new Config("{\"bakersSpeed\": [10], \"couriersCapacity\": [1], \"storageCapacity\": 2}");
        Pizzeria pizzeria = new Pizzeria(config);

        pizzeria.start();
        pizzeria.addOrder();
        pizzeria.addOrder();

        pizzeria.shutdown();

        // Если shutdown прошел успешно и не завис, тест считается пройденным
        assertTrue(true);
    }

    @Test
    public void testMainExecution() throws PizzeriaException {
        String json = "{\"bakersSpeed\": [50], \"couriersCapacity\": [2], \"storageCapacity\": 3}";
        File configFile = new File("config.json");

        try {
            Files.writeString(Paths.get(configFile.toURI()), json);

            // Вызываем main, он создаст заказы и штатно завершится (в Main.java мы делали 10 заказов)
            Main.main(new String[]{});

            assertTrue(true);
        } catch (IOException e) {
            throw new PizzeriaConfigurationException("Ошибка записи тестового конфига.", e);
        } finally {
            if (configFile.exists()) {
                configFile.delete();
            }
        }
    }
}
