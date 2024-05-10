package p2.sorts;

import cse332.exceptions.NotYetImplementedException;

import java.util.Comparator;
import java.util.Random;

public class QuickSort {
    public static <E extends Comparable<E>> void sort(E[] array) {
        QuickSort.sort(array, (x, y) -> x.compareTo(y));
    }

    public static <E> void sort(E[] array, Comparator<E> comparator) {
        sort(array, 0, array.length - 1, comparator);
    }

    private static <E> void sort(E[] array, int lo, int hi, Comparator<E> comparator) {
        if (lo < hi) {
            int pivot = partition(array, lo, hi, comparator);
            sort(array, lo, pivot - 1, comparator);
            sort(array, pivot + 1, hi, comparator);
        }
    }

    private static int pickPivot(int lo, int hi) {
        Random r = new Random();
        return r.nextInt(hi - lo + 1) + lo;
    }

    private static <E> int partition(E[] array, int lo, int hi, Comparator<E> comparator) {
        int pivotIndex = pickPivot(lo, hi);
        E pivot = array[pivotIndex];

        swap(array, pivotIndex, hi);
        int i = lo;
        int j = hi - 1;

        while (i <= j) {
            if (comparator.compare(array[j], pivot) > 0) {
                j--;
            } else if (comparator.compare(array[i], pivot) <= 0) {
                i++;
            } else {
                swap(array, i, j);
                i++;
                j--;
            }
        }

        swap(array, i, hi);
        return i;
    }

    private static <E> void swap(E[] array, int i, int j) {
        E temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
}
