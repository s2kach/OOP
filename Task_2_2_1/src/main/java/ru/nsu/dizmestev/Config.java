package ru.nsu.dizmestev;

import com.google.gson.Gson;

/**
 * Класс конфигурации пиццерии, заполняемый вручную из JSON строки.
 */
public class Config {
    private int[] bakersSpeed;
    private int[] couriersCapacity;
    private int storageCapacity;

    /**
     * Конструктор.
     */
    public Config() {}

    /**
     * Создает конфигурацию из JSON строки с помощью Gson.
     *
     * @param jsonContent
     * @return
     */
    public static Config fromJson(String jsonContent) {
        return new Gson().fromJson(jsonContent, Config.class);
    }

    /**
     * Возвращает массив скоростей готовки пекарей.
     *
     * @return массив скоростей в миллисекундах
     */
    public int[] getBakersSpeed() {
        return bakersSpeed;
    }

    /**
     * Возвращает массив вместимостей багажников курьеров.
     *
     * @return массив вместимостей
     */
    public int[] getCouriersCapacity() {
        return couriersCapacity;
    }

    /**
     * Возвращает максимальную вместимость склада.
     *
     * @return количество пицц
     */
    public int getStorageCapacity() {
        return storageCapacity;
    }
}
