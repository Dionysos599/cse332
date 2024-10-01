package datastructures.worklists;

import cse332.interfaces.worklists.FIFOWorkList;

import java.util.NoSuchElementException;

/**
 * See cse332/interfaces/worklists/FIFOWorkList.java
 * for method specifications.
 */
public class ListFIFOQueue<E> extends FIFOWorkList<E> {
    public static class ListNode<E> {
        public E work;
        public ListNode<E> next;

        // Constructs a ListNode with the given work
        public ListNode(E work) {
            this(work, null);
        }

        // Constructs a ListNode with the given work and next node
        public ListNode(E work, ListNode<E> next) {
            this.work = work;
            this.next = next;
        }
    }

    private ListNode<E> front;
    private ListNode<E> back;
    private int size;

    @Override
    public void add(E work) {
        ListNode<E> newNode = new ListNode<>(work);
        size++;
        if (back == null) { // empty queue
            front = newNode;
            back = newNode;
        } else {
            back.next = newNode;
            back = back.next;
        }
    }

    @Override
    public E peek() {
        if (!hasWork()) {
            throw new NoSuchElementException();
        }
        return front.work;
    }

    @Override
    public E next() {
        if (!hasWork()) {
            throw new NoSuchElementException();
        }
        E work = front.work;
        front = front.next;
        size--;

        // If queue is empty, set rear ptr as null
        if (size <= 0) {
            back = null;
        }
        return work;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        front = null;
        back = null;
        size = 0;
    }
}
