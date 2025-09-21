package ru.nsu.dizmestev;

import java.util.Scanner;

/**
 * Игрок.
 */
public class Player extends Participant {
    private final Scanner scanner = new Scanner(System.in);

    /**
     * Будет ли брать карту зависит от ответа в консоли.
     *
     * @return true если согласился брать
     */
    @Override
    public boolean shouldTakeCard() {
        System.out.println("Введите \"1\", чтобы взять карту, и \"0\", чтобы остановиться.");
        int choice = -1;
        boolean validInput = false;
        while (!validInput) {
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                if (choice == 0 || choice == 1) {
                    validInput = true;
                } else {
                    System.out.println("Неверный ввод. Пожалуйста, введите 0 или 1.");
                }
            } else {
                System.out.println("Неверный ввод. Пожалуйста, введите число 0 или 1.");
                scanner.next(); // Очищаем некорректный ввод
            }
        }
        scanner.nextLine(); // Очистка буфера
        return choice == 1;
    }
}