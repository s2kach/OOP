package ru.nsu.dizmestev;

import java.util.Arrays;

/**
 * Единая точка входа для запуска приложения в режиме Мастера или Воркера.
 */
public class Main {

    /**
     * Главный метод приложения, анализирующий аргументы командной строки.
     *
     * @param args Аргументы командной строки.
     */
    public static void main(String[] args) {
        if (args.length < 1) {
            printUsage();
            return;
        }

        switch (args[0].toLowerCase()) {
            case "master" -> runMaster(args);
            case "worker" -> runWorker(args);
            default -> printUsage();
        }
    }

    /**
     * Запускает приложение в режиме мастер-узла.
     *
     * @param args Аргументы командной строки.
     */
    private static void runMaster(String[] args) {
        int[] numbers = {
                20319251, 6997901, 6997927, 6997937, 17858849, 6997967,
                6998009, 6998029, 6998039, 20165149, 6998051, 6998053
        };

        System.out.println("Запуск Мастера. Исходный массив: " + Arrays.toString(numbers));
        MasterNode master = new MasterNode(numbers);

        try {
            master.start();
        } catch (NetworkException e) {
            System.err.println("Критическая ошибка мастера: " + e.getMessage());
        }
    }

    /**
     * Запускает приложение в режиме вычислительного узла (воркера).
     *
     * @param args Аргументы командной строки.
     */
    private static void runWorker(String[] args) {
        System.out.println("Запуск Воркера...");
        WorkerNode worker = new WorkerNode();

        try {
            worker.start();
        } catch (NetworkException e) {
            System.err.println("Критическая ошибка воркера: " + e.getMessage());
        }
    }

    /**
     * Выводит правила использования интерфейса командной строки.
     */
    private static void printUsage() {
        System.out.println("Использование:");
        System.out.println("  java -jar app.jar master");
        System.out.println("  java -jar app.jar worker");
    }
}