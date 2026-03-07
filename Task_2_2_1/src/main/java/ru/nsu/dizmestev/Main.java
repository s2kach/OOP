package ru.nsu.dizmestev;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Точка входа в программу
 */
public class Main {

    /**
     * Главный метод, читающий конфигурацию и запускающий симуляцию.
     *
     * @param args аргументы командной строки
     * @throws PizzeriaException в случае любых ошибок конфигурации или работы
     */
    public static void main(String[] args) throws PizzeriaException {
        String configJson;
        try {
            configJson = Files.readString(Paths.get("config.json"));
        } catch (IOException e) {
            throw new PizzeriaConfigurationException("Не удалось найти или "
                    + "прочитать файл config.json.", e);
        }

        Config config;
        try {
            config = new Config(configJson);
        } catch (Exception e) {
            throw new PizzeriaConfigurationException("Ошибка при парсинге JSON конфигурации.", e);
        }

        if (config.getBakersSpeed().length == 0 || config.getCouriersCapacity().length == 0
                || config.getStorageCapacity() <= 0) {
            throw new PizzeriaConfigurationException("Конфигурация пуста или "
                    + "содержит некорректные данные.", null);
        }

        Pizzeria pizzeria = new Pizzeria(config);
        pizzeria.start();

        try {

            for (int i = 0; i < 10; i++) {
                pizzeria.addOrder();
                Thread.sleep(500);
            }

            System.out.println("--- Прием заказов окончен. Завершаем работу... ---");
            pizzeria.shutdown();
            System.out.println("--- Все заказы выполнены. Пиццерия закрыта. ---");

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new PizzeriaInterruptedException("Главный поток симуляции был прерван.", e);
        }
    }
}
