package ru.nsu.g.izmestev.Task_1_1_1;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SortTest {

    @Test
    void sort() {
        int[] array = {1, 3, 2};
        Sort.sort(array);
        assertArrayEquals(new int[] {1, 2, 3}, array);

        int[] array2 = {1, 3, 2, 35, 78, 0, 990};
        Sort.sort(array2);
        assertArrayEquals(new int[] {0, 1, 2, 3, 35, 78, 990}, array2);
    }


}