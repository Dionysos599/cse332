package datastructures.worklists;

import cse332.exceptions.NotYetImplementedException;
import cse332.interfaces.worklists.FixedSizeFIFOWorkList;

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
        if (!(other instanceof CircularArrayFIFOQueue)) {
            throw new IllegalArgumentException("Expected type: CircularArrayFIFOQueue");
        }

        CircularArrayFIFOQueue<E> otherQueue = (CircularArrayFIFOQueue<E>) other;
        int minSize = Math.min(this.size, otherQueue.size);

        for (int i = 0; i < minSize; i++) {
            E thisElement = this.arr[(this.front + i) % this.arr.length];
            E otherElement = otherQueue.arr[(otherQueue.front + i) % otherQueue.arr.length];

            if (thisElement == null && otherElement != null) {
                return -1;
            } else if (thisElement != null && otherElement == null) {
                return 1;
            } else if (thisElement != null && otherElement != null) {
                int comparison = ((Comparable<E>) thisElement).compareTo(otherElement);
                if (comparison != 0) {
                    return comparison;
                }
            }
        }

        return Integer.compare(this.size, otherQueue.size);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (!(obj instanceof FixedSizeFIFOWorkList<?>)) {
            return false;
        } else {
            FixedSizeFIFOWorkList<E> other = (FixedSizeFIFOWorkList<E>) obj;
            return this.compareTo(other) == 0;
        }
    }

    @Override
    public int hashCode() {
        throw new NotYetImplementedException();
    }
}
