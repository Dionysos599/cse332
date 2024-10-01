package datastructures.dictionaries;

import cse332.datastructures.containers.Item;
import cse332.interfaces.misc.DeletelessDictionary;

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


        V oldVal = find(key); // if key exists, move to front
        if (oldVal != null) {  // if exists, update its value
            head.value = value;
            return oldVal;
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
        if (key == null)
            throw new IllegalArgumentException("Null key");

        Node<K, V> curr = head;
        while (curr != null) {
            if (curr.key.equals(key)) { // found key, move to front and return its value
                if (curr != head) {
                    Node<K, V> temp = head;
                    while (temp.next != curr) {
                        temp = temp.next;
                    }
                    temp.next = curr.next;
                    curr.next = head;
                    head = curr;
                }
                return curr.value;
            }

            curr = curr.next;
        }
        return null;
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
