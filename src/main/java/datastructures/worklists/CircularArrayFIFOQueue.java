package datastructures.worklists;

import cse332.exceptions.NotYetImplementedException;
import cse332.interfaces.worklists.FixedSizeFIFOWorkList;

import java.util.Arrays;
import java.util.NoSuchElementException;

/**
 * See cse332/interfaces/worklists/FixedSizeFIFOWorkList.java
 * for method specifications.
 */
@SuppressWarnings("unchecked")
public class CircularArrayFIFOQueue<E> extends FixedSizeFIFOWorkList<E> {

    private int front;
    private int back;
    private E[] arr;
    private int size;
    public CircularArrayFIFOQueue(int capacity) {
        super(capacity);
        arr = (E[])new Comparable[capacity];
    }

    @Override
    public void add(E work) {
        if (isFull())
            throw new IllegalStateException("Queue is full");

        arr[back] = work;
        back = (back + 1) % capacity();
        size++;
    }

    @Override
    public E peek() {
        return peek(0);
    }

    @Override
    public E peek(int i) {
        if (!hasWork())
            throw new NoSuchElementException();
        else if (i < 0 || i >= size())
            throw new IndexOutOfBoundsException();

        if (size() == 0)
            return null;

        return arr[(front + i) % capacity()];
    }

    @Override
    public E next() {
        if (!hasWork())
            throw new NoSuchElementException();

        E work = arr[front];
        front = (front + 1) % capacity();
        size--;
        return work;
    }

    @Override
    public void update(int i, E value) {
        if (!hasWork())
            throw new NoSuchElementException();
        else if (i < 0 || i >= size())
            throw new IndexOutOfBoundsException();

        arr[(front + i) % capacity()] = value;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        arr = (E[])new Comparable[capacity()];
        front = 0;
        back = 0;
        size = 0;
    }

    @Override
    public int compareTo(FixedSizeFIFOWorkList<E> other) {
        // You will implement this method in project 2. Leave this method unchanged for project 1.
        throw new NotYetImplementedException();
    }

    @Override
    public boolean equals(Object obj) {
        // You will finish implementing this method in project 2. Leave this method unchanged for project 1.
        if (this == obj) {
            return true;
        } else if (!(obj instanceof FixedSizeFIFOWorkList<?>)) {
            return false;
        } else {
            // Uncomment the line below for p2 when you implement equals
            // FixedSizeFIFOWorkList<E> other = (FixedSizeFIFOWorkList<E>) obj;

            // Your code goes here

            throw new NotYetImplementedException();
        }
    }

    @Override
    public int hashCode() {
        // You will implement this method in project 2. Leave this method unchanged for project 1.
        throw new NotYetImplementedException();
    }
}
