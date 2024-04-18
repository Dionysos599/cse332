package datastructures.dictionaries;

import cse332.datastructures.containers.Item;
import cse332.exceptions.NotYetImplementedException;
import cse332.interfaces.misc.DeletelessDictionary;
import datastructures.worklists.ListFIFOQueue;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * 1. The list is typically not sorted.
 * 2. Add new items to the front of the list.
 * 3. Whenever find or insert is called on an existing key, move it
 * to the front of the list. This means you remove the node from its
 * current position and make it the first node in the list.
 * 4. You need to implement an iterator. The iterator SHOULD NOT move
 * elements to the front.  The iterator should return elements in
 * the order they are stored in the list, starting with the first
 * element in the list. When implementing your iterator, you should
 * NOT copy every item to another dictionary/list and return that
 * dictionary/list's iterator.
 */
public class MoveToFrontList<K, V> extends DeletelessDictionary<K, V> {

    private Node<K, V> head;

    @Override
    public V insert(K key, V value) {
        if (key == null || value == null)
            throw new IllegalArgumentException("Null key/value");

        Node<K, V> curr = head;
        while (curr != null) { // find if key exists
            if (curr.key.equals(key)) {
                V oldVal = curr.value;
                curr.value = value;
                moveFront(curr);
                return oldVal;
            }
            curr = curr.next;
        }

        // not exist, create and add to front
        Node<K, V> newNode = new Node<>(key, value);
        newNode.next = head;
        head = newNode;
        size++;
        return null;
    }

    @Override
    public V find(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }

        Node<K, V> curr = head;
        while (curr != null) {
            if (curr.key.equals(key)) {
                // found key, move front and return its value
                moveFront(curr);
                return curr.value;
            }
            curr = curr.next;
        }
        return null;
    }

    private void moveFront(Node<K, V> curr) {
        if (curr != head) {
            Node<K, V> prev = head;
            while (prev.next != curr) {
                prev = prev.next;
            }
            prev.next = curr.next;
            curr.next = head;
            head = curr;
        }
    }

    @Override
    public Iterator<Item<K, V>> iterator() {
        return new MoveToFrontListIterator();
    }

    private class MoveToFrontListIterator implements Iterator<Item<K, V>> {
        private Node<K, V> current = head;

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public Item<K, V> next() {
            if (!hasNext())
                throw new NoSuchElementException();

            Item<K, V> item = new Item<>(current.key, current.value);
            current = current.next;
            return item;
        }
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
