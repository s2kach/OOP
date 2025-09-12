package ru.nsu.dizmestev;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

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
    void sort_sorted() {
        int[] array = {1, 2, 3, 4, 5};
        Sort.sort(array);
        assertArrayEquals(new int[] {1, 2, 3, 4, 5}, array);
    }

    @Test
    void sort_below_zero() {
        int[] array = {-1, -2, -3, -4, -5};
        Sort.sort(array);
        assertArrayEquals(new int[] {-5, -4, -3, -2, -1}, array);
    }

    @Test
    void sort_repetitions() {
        int[] array = {1, 2, 1, 1, 2};
        Sort.sort(array);
        assertArrayEquals(new int[] {1, 1, 1, 2, 2}, array);
    }

    @Test
    void sort_empty() {
        int[] array = {};
        Sort.sort(array);
        assertArrayEquals(new int[] {}, array);
    }

    @Test
    void sort_null() {
        Sort.sort(null);
    }

    @Test
    void sort_one_elem() {
        int[] array = {1};
        Sort.sort(array);
        assertArrayEquals(new int[] {1}, array);
    }

}