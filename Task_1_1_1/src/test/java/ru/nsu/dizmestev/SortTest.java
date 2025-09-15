package ru.nsu.dizmestev;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.util.Random;
import org.junit.jupiter.api.Test;

class SortTest {

    @Test
    void sort() {
        int[] array = {1, 3, 2};
        Sort.sort(array);
        assertArrayEquals(new int[] {1, 2, 3}, array);
    }

    @Test
    void sort2() {
        int[] array = {1, 3, 2, 35, 78, 0, 990};
        Sort.sort(array);
        assertArrayEquals(new int[] {0, 1, 2, 3, 35, 78, 990}, array);
    }

    @Test
    void sortSorted() {
        int[] array = {1, 2, 3, 4, 5};
        Sort.sort(array);
        assertArrayEquals(new int[] {1, 2, 3, 4, 5}, array);
    }

    @Test
    void sortBelowZero() {
        int[] array = {-1, -2, -3, -4, -5};
        Sort.sort(array);
        assertArrayEquals(new int[] {-5, -4, -3, -2, -1}, array);
    }

    @Test
    void sortRepetitions() {
        int[] array = {1, 2, 1, 1, 2};
        Sort.sort(array);
        assertArrayEquals(new int[] {1, 1, 1, 2, 2}, array);
    }

    @Test
    void sortEmpty() {
        int[] array = {};
        Sort.sort(array);
        assertArrayEquals(new int[] {}, array);
    }

    @Test
    void sortNull() {
        Sort.sort(null);
    }

    @Test
    void sortOneElem() {
        int[] array = {1};
        Sort.sort(array);
        assertArrayEquals(new int[] {1}, array);
    }

    @Test
    void checkTime() {
        System.out.printf("================================================================\n");
        for (int size = 100; size <= 10000000; size *= 10) {
            Random random = new Random();
            int[] array = new int[size];
            System.out.printf("Size: %d\n", size);
            for (int i = 0; i < size; i++) {
                array[i] = random.nextInt(size);
            }

            long timeStart = System.nanoTime();
            Sort.sort(array);
            System.out.printf("Sorted in %.10f seconds\n",
                    (double) (System.nanoTime() - timeStart) / 1000000000);
            System.out.printf("================================================================\n");
        }
    }
}