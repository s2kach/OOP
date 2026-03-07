package ru.nsu.dizmestev;

/**
 * Класс конфигурации пиццерии, заполняемый вручную из JSON строки.
 */
public class Config {
    private final int[] bakersSpeed;
    private final int[] couriersCapacity;
    private final int storageCapacity;

    /**
     * Конструктор, инициализирующий поля через парсинг строки.
     *
     * @param jsonContent содержимое конфигурационного файла
     */
    public Config(String jsonContent) {
        this.bakersSpeed = JsonParser.getIntArray(jsonContent, "bakersSpeed");
        this.couriersCapacity = JsonParser.getIntArray(jsonContent, "couriersCapacity");
        this.storageCapacity = JsonParser.getInt(jsonContent, "storageCapacity");
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
