package ru.nsu.g.izmestev.Task_1_1_1;

public class Sort {
    private static void heapify(int[] array, int n, int ind) {
        int toswap = ind;
        int left = 2 * ind + 1;
        int right = 2 * ind + 2;
        if (left < n && array[left] > array[toswap]) {
            toswap = left;
        }
        if (right < n && array[right] > array[toswap]) {
            toswap = right;
        }
        if (toswap != ind) {
            int temp = array[ind];
            array[ind] = array[toswap];
            array[toswap] = temp;
            heapify(array, n, toswap);
        }
    }

    public static void sort(int[] array) {
        int len = array.length;

        for (int i = len / 2 - 1; i >= 0; i--) {
            heapify(array, len, i);
        }

        for (int i = len - 1; i > 0; i--) {
            int temp = array[0];
            array[0] = array[i];
            array[i] = temp;

            heapify(array, i, 0);
        }
    }
}