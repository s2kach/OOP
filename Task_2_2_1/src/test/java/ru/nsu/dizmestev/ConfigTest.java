package ru.nsu.dizmestev;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;

/**
 * Тесты для проверки логики извлечения данных из JSON и создания конфигурации.
 */
public class ConfigTest {
    @Test
    public void testValidJsonParsing() {
        String json = "{\"bakersSpeed\": [1000, 2000], \"couriersCapacity\": [2],"
                + " \"storageCapacity\": 5}";
        Gson gson = new Gson();
        Config config = gson.fromJson(json, Config.class);

        assertNotNull(config);
        assertArrayEquals(new int[]{1000, 2000}, config.getBakersSpeed());
        assertArrayEquals(new int[]{2}, config.getCouriersCapacity());
        assertEquals(5, config.getStorageCapacity());
    }

    @Test
    public void testEmptyJsonParsing() {
        String json = "{}";
        Gson gson = new Gson();
        Config config = gson.fromJson(json, Config.class);

        assertNotNull(config);
        assertNull(config.getBakersSpeed());
        assertNull(config.getCouriersCapacity());
        assertEquals(0, config.getStorageCapacity());
    }
}