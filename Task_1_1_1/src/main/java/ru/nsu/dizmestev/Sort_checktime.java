package ru.nsu.dizmestev;

import java.util.Random;

/**
 * This class is used to check time of sort.
 */
public class Sort_checktime {
    /**
     * Main function.
     *
     * @param args no args in my code
     */
    public static void main(String[] args) {
        System.out.printf("================================================================\n");
        for (int size = 100; size <= 10000000; size*=10) {
            Random random = new Random();
            int[] array = new int[size];
            System.out.printf("Size: %d\n", size);
            for (int i = 0; i < size; i++) {
                array[i] = random.nextInt(size);
            }

            long timeStart = System.nanoTime();
            Sort.sort(array);
            System.out.printf("Sorted in %.10f seconds\n", (double) (System.nanoTime() - timeStart) / 1000000000);
            System.out.printf("================================================================\n");
        }
    }
}
