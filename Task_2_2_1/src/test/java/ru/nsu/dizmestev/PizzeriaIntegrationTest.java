package ru.nsu.dizmestev;

import static org.junit.jupiter.api.Assertions.assertThrows;
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
        Config config = new Config("{\"bakersSpeed\": [10], \"couriersCapacity\": [1],"
                + " \"storageCapacity\": 2}");
        Pizzeria pizzeria = new Pizzeria(config);

        pizzeria.start();
        pizzeria.addOrder();
        pizzeria.addOrder();

        pizzeria.shutdown();

        assertTrue(true);
    }

    @Test
    public void testMainExecution() throws PizzeriaException {
        String json = "{\"bakersSpeed\": [50], \"couriersCapacity\": [2], \"storageCapacity\": 3}";
        File configFile = new File("config.json");

        try {
            Files.writeString(Paths.get(configFile.toURI()), json);

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

    @Test
    public void testMainMissingConfigFile() {
        File configFile = new File("config.json");
        // Убеждаемся, что файла нет
        if (configFile.exists()) {
            configFile.delete();
        }

        assertThrows(PizzeriaConfigurationException.class, () -> Main.main(new String[]{}));
    }

    @Test
    public void testMainInvalidJsonConfig() throws PizzeriaConfigurationException {
        File configFile = new File("config.json");
        try {
            Files.writeString(Paths.get(configFile.toURI()), "это не json");
            assertThrows(PizzeriaConfigurationException.class, () -> Main.main(new String[]{}));
        } catch (IOException e) {
            throw new PizzeriaConfigurationException("Ошибка подготовки тестового файла.", e);
        } finally {
            if (configFile.exists()) {
                configFile.delete();
            }
        }
    }

    @Test
    public void testMainEmptyConfigData() throws PizzeriaConfigurationException {
        File configFile = new File("config.json");
        String emptyConfig = "{\"bakersSpeed\": [], \"couriersCapacity\": [],"
                + " \"storageCapacity\": 0}";
        try {
            Files.writeString(Paths.get(configFile.toURI()), emptyConfig);
            assertThrows(PizzeriaConfigurationException.class, () -> Main.main(new String[]{}));
        } catch (IOException e) {
            throw new PizzeriaConfigurationException("Ошибка подготовки тестового файла.", e);
        } finally {
            if (configFile.exists()) {
                configFile.delete();
            }
        }
    }
}
