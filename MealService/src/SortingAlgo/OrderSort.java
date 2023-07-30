package SortingAlgo;

import java.util.Comparator;

public class OrderSort<T extends Comparable<T>> {

    public static <T> void sort(T[] array, Comparator<T> comparator) {
        mergeSort(array, 0, array.length - 1, comparator);
    }

    private static <T> void mergeSort(T[] array, int start, int end, Comparator<T> comparator) {
        if (start == end) {
            return;
        }
        int mid = (start + end) / 2;
        mergeSort(array, start, mid, comparator);
        mergeSort(array, mid + 1, end, comparator);
        merge(array, start, mid, end, comparator);
    }

    private static <T> void merge(T[] array, int start, int mid, int end, Comparator<T> comparator) {
        int subArrLength = end - start + 1;
        Object[] values = new Object[subArrLength];

        int startIndex = start;

        int middleIndex = mid + 1;

        int index = 0;

        while (startIndex <= mid && middleIndex <= end) {
            if (comparator.compare(array[startIndex], array[middleIndex]) < 0) {
                values[index] = array[startIndex];
                startIndex++;
            } else {
                values[index] = array[middleIndex];
                middleIndex++;
            }
            index++;
        }

        while (startIndex <= mid) {
            values[index] = array[startIndex];
            startIndex++;
            index++;
        }
        while (middleIndex <= end) {
            values[index] = array[middleIndex];
            middleIndex++;
            index++;
        }

        for (index = 0; index < subArrLength; index++) {
            array[start + index] = (T) values[index];
        }
    }
}
