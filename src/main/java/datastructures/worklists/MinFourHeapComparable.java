package datastructures.worklists;

import cse332.exceptions.NotYetImplementedException;
import cse332.interfaces.worklists.PriorityWorkList;

import java.sql.SQLOutput;
import java.util.NoSuchElementException;

/**
 * See cse332/interfaces/worklists/PriorityWorkList.java
 * for method specifications.
 */
@SuppressWarnings("unchecked")
public class MinFourHeapComparable<E extends Comparable<E>> extends PriorityWorkList<E> {

    private int size;
    private E[] data; // Do not change the name of this field; the tests rely on it to work correctly.
    private final int DEFAULT_SIZE = 16;

    public MinFourHeapComparable() {
        data = (E[]) new Comparable[DEFAULT_SIZE];
    }

    private int parent(int i) {
        return (i - 1) / 4;
    }

    private int leftChild(int i) {
        return 4 * i + 1;
    }

    private int parcolateUp(int hole, E work) {
        while (hole > 1 && work.compareTo(data[parent(hole)]) < 0) {
            data[hole] = data[parent(hole)];
            hole = parent(hole);
        }
        return hole;
    }

    private int parcolateDown(int hole, E work) {
        int target = hole;
        while (leftChild(target) <= size) {
            int left = leftChild(target);
            int min = left;

            for (int i = 1; i <= 4; i++) {
                int childIndex = left + i;
                if (childIndex <= size && data[childIndex] != null && data[childIndex].compareTo(data[min]) < 0) {
                    min = childIndex;
                }
            }

            if (data[min] != null && data[min].compareTo(work) < 0) {
                data[target] = data[min];
                target = min;
            } else {
                break;
            }
        }

        data[target] = work;
        return target;
    }

    private void resize() {
        E[] newData = (E[]) new Comparable[data.length * 2];
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

        int hole = parcolateUp(size, work);
        data[hole] = work;
        size++;
    }

    /**
     * Returns a view to the next element of the worklist.
     *
     * @precondition hasWork() is true
     * @postcondition return(peek()) is return(next())
     * @postcondition the structure of this worklist remains unchanged.
     * @throws NoSuchElementException
     *             if hasWork() is false
     * @return the next element in this worklist
     */
    @Override
    public E peek() {
        if (!hasWork())
            throw new NoSuchElementException();

        return data[0];
    }

    /**
     * Returns and removes the next element of the worklist
     *
     * @precondition hasWork() is true
     * @postcondition return(next()) + after(next()) == before(next())
     * @postcondition after(size()) + 1 == before(size())
     * @throws NoSuchElementException
     *             if hasWork() is false
     * @return the next element in this worklist
     */
    @Override
    public E next() {
        if (!hasWork())
            throw new NoSuchElementException();

        E result = data[0];
        int hole = parcolateDown(0, data[size - 1]);
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
