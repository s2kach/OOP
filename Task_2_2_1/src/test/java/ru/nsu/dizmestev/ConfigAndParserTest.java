package ru.nsu.dizmestev;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Тесты для проверки логики извлечения данных из JSON и создания конфигурации.
 */
public class ConfigAndParserTest {

    @Test
    public void testValidJsonParsing() {
        String json = "{\"bakersSpeed\": [1000, 2000], \"couriersCapacity\": [2], \"storageCapacity\": 5}";
        Config config = new Config(json);

        assertArrayEquals(new int[]{1000, 2000}, config.getBakersSpeed());
        assertArrayEquals(new int[]{2}, config.getCouriersCapacity());
        assertEquals(5, config.getStorageCapacity());
    }

    @Test
    public void testEmptyJsonParsing() {
        String json = "{\"bakersSpeed\": [], \"couriersCapacity\": []}";
        Config config = new Config(json);

        assertArrayEquals(new int[]{}, config.getBakersSpeed());
        assertArrayEquals(new int[]{}, config.getCouriersCapacity());
        assertEquals(0, config.getStorageCapacity());
    }

    @Test
    public void testParserGetInt() {
        String json = "{\"testKey\": 42}";
        int result = JsonParser.getInt(json, "testKey");
        assertEquals(42, result);
    }
}