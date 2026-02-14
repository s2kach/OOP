package ru.nsu.dizmestev;

import static java.lang.Math.sqrt;

/**
 * Класс для математических проверок чисел.
 */
public class PrimeChecker {

    /**
     * Проверяет, является ли число простым.
     *
     * @param number Число для проверки.
     * @return true, если число простое, иначе false.
     */
    public static boolean isPrime(int number) {
        if (number < 2) {
            return false;
        }
        for (int i = 2; i <= sqrt(number); i++) {
            if (number % i == 0) {
                return false;
            }
        }
        return true;
    }
}
