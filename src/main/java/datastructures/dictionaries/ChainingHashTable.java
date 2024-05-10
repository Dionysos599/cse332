package datastructures.dictionaries;

import cse332.datastructures.containers.Item;
import cse332.interfaces.misc.DeletelessDictionary;
import cse332.interfaces.misc.Dictionary;

import java.util.Iterator;
import java.util.function.Supplier;

/**
 *   <li>You must implement a generic chaining hashtable. You may not
 *   restrict the size of the input domain (i.e., it must accept
 *   any key) or the number of inputs (i.e., it must grow as necessary). </li>
 *
 *   <li>ChainingHashTable should rehash as appropriate (use load factor as shown in lecture!).</li>
 *
 *   <li>ChainingHashTable must resize its capacity into prime numbers via given PRIME_SIZES list.
 *   Past this, it should continue to resize using some other mechanism (primes not necessary).</li>
 *
 *   <li>When implementing your iterator, you should NOT copy every item to another
 *   dictionary/list and return that dictionary/list's iterator.</li>
 */
@SuppressWarnings("unchecked")
public class ChainingHashTable<K, V> extends DeletelessDictionary<K, V> {
    private Supplier<Dictionary<K, V>> newChain;
    private Dictionary<K, V>[] table;
    private int capacity;
    private final double loadFactor;

    static final int[] PRIME_SIZES =
            {11, 23, 47, 97, 193, 389, 773, 1549, 3089, 6173, 12347, 24697, 49393, 98779, 197573, 395147};

    public ChainingHashTable(Supplier<Dictionary<K, V>> newChain) {
        this.newChain = newChain;
        this.size = 0;
        this.capacity = PRIME_SIZES[0];
        this.loadFactor = 0.5;
        this.table = (Dictionary<K, V>[]) new Dictionary[this.capacity];
        for (int i = 0; i < this.capacity; i++) {
            this.table[i] = this.newChain.get();
        }
    }

    /**
     * Inserts the specified key-value pair into the dictionary. If the key is already
     * present in the dictionary, the value associated with the key is replaced with the
     * specified value.
     *
     * @param key
     *            key with which the specified value is to be associated
     * @param value
     *              value to be associated with the specified key
     *
     * @return  the old value associated with the key, or null if the key did not
     */
    @Override
    public V insert(K key, V value) {
        if (key == null || value == null)
            throw new IllegalArgumentException("Null key/value");

        if (size >= loadFactor * capacity) {
            resize();
        }

        int index = hash(key);
        if (table[index] == null) {
            Dictionary<K, V> dict = newChain.get();
            dict.insert(key, value);
            table[index] = dict;
            size++;
            return null;
        } else {
            V oldVal = table[index].find(key);
            if (oldVal != null) {
                table[index].insert(key, value);
                return oldVal;
            } else {
                table[index].insert(key, value);
                size++;
                return null;
            }
        }
    }

    /**
     * Returns the value to which the specified key is mapped, or null if this dictionary
     * contains no mapping for the key.
     *
     * @param key
     *            the key whose associated value is to be returned
     *
     * @return  the value to which the specified key is mapped, or null if this dictionary
     *          contains no mapping for the key
     */
    @Override
    public V find(K key) {
        if(key == null) { return null; }
        int index = hash(key);
        if (table[index] == null) {
            return null;
        } else {
            return table[index].find(key);
        }
    }

    @Override
    public Iterator<Item<K, V>> iterator() {
        return new ChainingHashTableIterator();
    }

    private void resize() {
        int newCapacity = this.capacity * 2;
        for (int primeSize : PRIME_SIZES) {
            if (primeSize > newCapacity) {
                newCapacity = primeSize;
                break;
            }
        }

        Dictionary<K, V>[] oldTable = this.table;
        this.capacity = newCapacity;
        this.table = (Dictionary<K, V>[]) new Dictionary[this.capacity];
        for (int i = 0; i < this.capacity; i++) {
            table[i] = this.newChain.get();
        }
        this.size = 0;

        for (Dictionary<K, V> chain : oldTable) {
            for (Item<K, V> item : chain) {
                insert(item.key, item.value);
            }
        }
        capacity = newCapacity;
    }

    private int hash(K key) {
        return Math.abs(key.hashCode()) % capacity;
    }

    private class ChainingHashTableIterator implements Iterator<Item<K, V>> {
        private int chainIndex;
        private Iterator<Item<K, V>> chainIterator;

        public ChainingHashTableIterator() {
            chainIndex = 0;
            advanceToNextNonEmptyChain();
            if (chainIndex < capacity) {
                chainIterator = table[chainIndex].iterator();
            } else {
                chainIterator = null;
            }
        }

        @Override
        public boolean hasNext() {
            if (chainIterator != null && chainIterator.hasNext())
                return true;

            while (chainIndex < capacity - 1) {
                chainIndex++;
                if (table[chainIndex] != null && table[chainIndex].iterator().hasNext()) {
                    chainIterator = table[chainIndex].iterator();
                    return true;
                }
            }

            return false;
        }

        @Override
        public Item<K, V> next() {
            if (!hasNext())
                throw new java.util.NoSuchElementException();
            return chainIterator.next();
        }

        private void advanceToNextNonEmptyChain() {
            while (chainIndex < capacity && (table[chainIndex] == null || table[chainIndex].isEmpty())) {
                chainIndex++;
            }
        }
    }
}
