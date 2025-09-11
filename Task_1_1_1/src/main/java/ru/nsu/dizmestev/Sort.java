package ru.nsu.dizmestev;

public class Sort {
    /**
     * Recoursive building a heap in array
     *
     * @param array  all elements
     * @param n      size
     * @param ind    current index
     */
    private static void heapify(int[] array, int n, int ind) {
        while (true) {
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
                ind = toswap;
            }
            else {
                break;
            }
        }
    }

    /**
     * Sorting function using heaps
     *
     * @param array array
     */
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