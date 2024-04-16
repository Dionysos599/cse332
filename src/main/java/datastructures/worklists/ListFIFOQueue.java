package datastructures.worklists;

import cse332.exceptions.NotYetImplementedException;
import cse332.interfaces.worklists.FIFOWorkList;

import java.util.NoSuchElementException;

/**
 * See cse332/interfaces/worklists/FIFOWorkList.java
 * for method specifications.
 */
public class ListFIFOQueue<E> extends FIFOWorkList<E> {

    private ListNode front;
    private ListNode back;
    private int size;

    public ListFIFOQueue() {}

    @Override
    public void add(E work) {
        ListNode newNode = new ListNode(work);
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
        return front.data;
    }

    @Override
    public E next() {
        if (!hasWork()) {
            throw new NoSuchElementException();
        }
        E work = front.data;
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

    private class ListNode {

        public E data;
        public ListNode next;

        public ListNode(E data) {
            this.data = data;
            next = null;
        }

    }

}
