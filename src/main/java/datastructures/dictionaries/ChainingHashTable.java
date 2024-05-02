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
    private int size;
    private int capacity;
    private final double loadFactor;

    static final int[] PRIME_SIZES =
            {11, 23, 47, 97, 193, 389, 773, 1549, 3089, 6173, 12347, 24697, 49393, 98779, 197573, 395147};

    public ChainingHashTable(Supplier<Dictionary<K, V>> newChain) {
        this.newChain = newChain;
        this.size = 0;
        this.capacity = PRIME_SIZES[0];
        this.loadFactor = 0.75;
        this.table = (Dictionary<K, V>[]) new Dictionary[this.capacity];
        for (int i = 0; i < this.capacity; i++) {
            this.table[i] = this.newChain.get();
        }
    }

    @Override
    public V insert(K key, V value) {
        if (key == null || value == null)
            throw new IllegalArgumentException("Null key/value");

        if (this.size >= this.loadFactor * this.capacity) {
            this.rehash();
        }

        int index = this.hash(key);
        V oldValue = this.table[index].insert(key, value);
        if (oldValue == null) {
            this.size++;
        }
        return oldValue;
    }

    @Override
    public V find(K key) {
        if (key == null)
            throw new IllegalArgumentException("Null key");

        int index = this.hash(key);
        return this.table[index].find(key);
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public Iterator<Item<K, V>> iterator() {
        return new ChainingHashTableIterator();
    }

    private void rehash() {
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
            this.table[i] = this.newChain.get();
        }
        this.size = 0;

        for (Dictionary<K, V> chain : oldTable) {
            for (Item<K, V> item : chain) {
                this.insert(item.key, item.value);
            }
        }
    }

    private int hash(K key) {
        return Math.abs(key.hashCode()) % this.capacity;
    }

    private class ChainingHashTableIterator implements Iterator<Item<K, V>> {
        private int chainIndex;
        private Iterator<Item<K, V>> chainIterator;

        public ChainingHashTableIterator() {
            this.chainIndex = 0;
            this.chainIterator = ChainingHashTable.this.table[0].iterator();
        }

        @Override
        public boolean hasNext() {
            if (this.chainIterator.hasNext())
                return true;

            while (this.chainIndex < ChainingHashTable.this.capacity - 1) {
                this.chainIndex++;
                this.chainIterator = ChainingHashTable.this.table[this.chainIndex].iterator();
                if (this.chainIterator.hasNext()) {
                    return true;
                }
            }

            return false;
        }

        @Override
        public Item<K, V> next() {
            return this.chainIterator.next();
        }
    }
}
