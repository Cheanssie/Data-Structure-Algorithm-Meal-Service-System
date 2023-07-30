package SortingAlgo;

import java.util.Comparator;

public class VoucherSort {

    public static <T> void bubbleSort(T[] anArray, int size, Comparator<T> comparator) {
        boolean sorted = false;

        for (int pass = 1; pass < size && !sorted; pass++) {
            sorted = true;
            for (int index = 0; index < size - pass; index++) {
                if (comparator.compare(anArray[index], anArray[index + 1]) > 0) {
                    swap(anArray, index, index + 1);
                    sorted = false;
                }

            }
        }
    }

    public static <T> void swap(T[] anArray, int first, int second) {
        T temporary = anArray[first];
        anArray[first] = anArray[second];
        anArray[second] = temporary;
    }

}
