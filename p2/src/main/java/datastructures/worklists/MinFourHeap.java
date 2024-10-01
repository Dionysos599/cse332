package datastructures.worklists;

import cse332.interfaces.worklists.PriorityWorkList;

import java.util.Comparator;
import java.util.NoSuchElementException;

/**
 * See cse332/interfaces/worklists/PriorityWorkList.java
 * for method specifications.
 */
@SuppressWarnings("unchecked")
public class MinFourHeap<E> extends PriorityWorkList<E> {
    /* Do not change the name of this field; the tests rely on it to work correctly. */
    private E[] data;
    private int size;
    private final int DEFAULT_SIZE = 16;
    private Comparator<E> comparator;

    public MinFourHeap(Comparator<E> c) {
        this.data = (E[]) new Object[DEFAULT_SIZE];
        this.size = 0;
        this.comparator = c;
    }

    private int percolateUp(int hole, E work) {
        while (hole > 0 && comparator.compare(work, data[(hole - 1) / 4]) < 0) {
            data[hole] = data[(hole - 1) / 4];
            hole = (hole - 1) / 4;
        }
        return hole;
    }

    private int percolateDown(E work) {
        int hole = 0;
        while (4 * hole + 1 < size) {
            int target = 4 * hole + 1;
            for (int i = 2; i <= 4; i++) {
                if (4 * hole + i >= size) {
                    break;
                }
                if (comparator.compare(data[4 * hole + i], data[target]) < 0) {
                    target = 4 * hole + i;
                }
            }

            if (comparator.compare(data[target], work) < 0) {
                data[hole] = data[target];
                hole = target;
            } else {
                break;
            }
        }

        return hole;
    }

    private void resize() {
        E[] newData = (E[]) new Object[data.length * 4];
        for (int i = 0; i < data.length; i++) {
            newData[i] = data[i];
        }
        data = newData;
    }

    @Override
    public boolean hasWork() {
        return size > 0;
    }

    @Override
    public void add(E work) {
        if (size == data.length)
            resize();

        int hole = percolateUp(size, work);
        data[hole] = work;
        size++;
    }

    @Override
    public E peek() {
        if (!hasWork())
            throw new NoSuchElementException();

        return data[0];
    }

    @Override
    public E next() {
        if (!hasWork())
            throw new NoSuchElementException();

        E result = data[0];
        int hole = percolateDown(data[size - 1]);
        data[hole] = data[size - 1];
        size--;
        return result;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        data = (E[]) new Comparable[DEFAULT_SIZE];
        size = 0;
    }
}
