package SortingAlgo;

import java.util.Comparator;

public class MealPackageSort {

    public static final int ASC = 1;
    public static final int DES = 2;

    private static <T> void swap(T[] anArray, int i, int j) {
        T tempHolder = anArray[i];
        anArray[i] = anArray[j];
        anArray[j] = tempHolder;
    }

    //QuickSorting
    public static <T> void quickSort(T[] anArray, int arrayLength, Comparator<T> comparator, int sortingOrder) {
        quickSort(anArray, 0, arrayLength - 1, comparator, sortingOrder);
    }

    private static <T> void quickSort(T[] anArray, int firstIndex, int lastIndex, Comparator<T> comparator, int sortingOrder) {
        if (firstIndex < lastIndex) {
            int pivotIndex = partition(anArray, firstIndex, lastIndex, comparator, sortingOrder);
            quickSort(anArray, firstIndex, pivotIndex - 1, comparator, sortingOrder);
            quickSort(anArray, pivotIndex + 1, lastIndex, comparator, sortingOrder);

        }
    }

    private static <T> int partition(T[] anArray, int firstIndex, int lastIndex, Comparator<T> comparator, int sortingOrder) {
        int pivotIndex = lastIndex;
        T pivot = anArray[lastIndex];
        int indexFromLeft = firstIndex;
        int indexFromRight = lastIndex - 1;
        boolean sorted = false;

        while (!sorted) {

            if (sortingOrder == ASC) {
                while (indexFromLeft < lastIndex && comparator.compare(anArray[indexFromLeft], pivot) < 0) {
                    indexFromLeft++;
                }
                while (indexFromRight > firstIndex && comparator.compare(anArray[indexFromRight], pivot) > 0) {
                    indexFromRight--;
                }
            } else if (sortingOrder == DES) {
                while (indexFromLeft < lastIndex && comparator.compare(anArray[indexFromLeft], pivot) > 0) {
                    indexFromLeft++;
                }
                while (indexFromRight > firstIndex && comparator.compare(anArray[indexFromRight], pivot) < 0) {
                    indexFromRight--;
                }
            }

                if (indexFromLeft < indexFromRight) {
                    swap(anArray, indexFromLeft, indexFromRight);
                    indexFromLeft++;
                    indexFromRight--;
                } else {
                    sorted = true;
                }
            }
            swap(anArray, pivotIndex, indexFromLeft);
            pivotIndex = indexFromLeft;
            return pivotIndex;
        }
    }
