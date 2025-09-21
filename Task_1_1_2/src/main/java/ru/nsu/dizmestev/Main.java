package ru.nsu.dizmestev;

import java.util.Scanner;


/**
 * Вход в программу будет с этого класса.
 */
public class Main {
    /**
     * Вход в программу.
     *
     * @param args аргументы не нужны будут
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Добро пожаловать в Блэкджек!");

        int deckCount = 1;
        boolean validInput = false;

        while (!validInput) {
            System.out.print("Сколько колод карт используем в игре? (1-8): ");

            if (scanner.hasNextInt()) {
                deckCount = scanner.nextInt();
                if (deckCount >= 1 && deckCount <= 8) {
                    validInput = true;
                } else {
                    System.out.println("Неверный ввод. Пожалуйста, введите число от 1 до 8.");
                }
            } else {
                System.out.println("Неверный ввод. Пожалуйста, введите число.");
                scanner.next();
            }
        }
        scanner.nextLine();

        DeckProvider provider = new RandomDeckProvider(deckCount);
        Game game = new Game(provider, scanner);
        game.start();
        scanner.close();
    }
}