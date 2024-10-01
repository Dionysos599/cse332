package datastructures.worklists;

import cse332.exceptions.NotYetImplementedException;
import cse332.interfaces.worklists.LIFOWorkList;

import java.util.NoSuchElementException;

/**
 * See cse332/interfaces/worklists/LIFOWorkList.java
 * for method specifications.
 */
public class ArrayStack<E> extends LIFOWorkList<E> {
    private E[] arr;
    private int size; // ptr to the next position from the top of stack

    public ArrayStack() {
        arr = (E[]) new Object[10];
    }

    @Override
    public void add(E work) {
        if (size == arr.length) {
            E[] newArr = (E[]) new Object[arr.length * 2];
            for (int i = 0; i < arr.length; i++) {
                newArr[i] = arr[i];
            }
            arr = newArr;
        }
        arr[size++] = work;
    }

    @Override
    public E peek() {
        if (!hasWork()) {
            throw new NoSuchElementException();
        }
        return arr[size - 1];
    }

    @Override
    public E next() {
        if (!hasWork()) {
            throw new NoSuchElementException();
        }
        E work = arr[--size];
        arr[size] = null;
        return work;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        arr = (E[]) new Object[10];
        size = 0;
    }
}
