package datastructures.dictionaries;

import cse332.datastructures.containers.Item;
import cse332.interfaces.trie.TrieMap;
import cse332.types.BString;
import cse332.interfaces.misc.Dictionary;
import cse332.interfaces.misc.SimpleIterator;

import java.util.AbstractMap;
import java.util.Iterator;
import java.util.Map.Entry;

/**
 * See {@link TrieMap}
 * and {@link cse332.interfaces.misc.Dictionary}
 * for method specifications.
 */
@SuppressWarnings("unchecked")
public class HashTrieMap<A extends Comparable<A>, K extends BString<A>, V> extends TrieMap<A, K, V> {
    public class HashTrieNode extends TrieNode<Dictionary<A, HashTrieNode>, HashTrieNode> {
        public HashTrieNode() {
            this(null);
        }

        public HashTrieNode(V value) {
            this.pointers = new ChainingHashTable<>(AVLTree::new);
            this.value = value;
        }

        /**
         * Iterator for the HashTrieNode
         *
         * @return Iterator for the HashTrieNode
         */
        @Override
        public Iterator<Entry<A, HashTrieMap<A, K, V>.HashTrieNode>> iterator() {
            return new HashTrieNodeIterator();
        }

        /**
         * @see SimpleIterator
         * @see Entry
         */
        private class HashTrieNodeIterator extends SimpleIterator<Entry<A, HashTrieNode>> {
            private Iterator<Item<A, HashTrieMap<A, K, V>.HashTrieNode>> iter;

            public HashTrieNodeIterator() {
                iter = pointers.iterator();
            }

            @Override
            public boolean hasNext() {
                return iter.hasNext();
            }

            @Override
            public Entry<A, HashTrieMap<A, K, V>.HashTrieNode> next() {
                Item<A, HashTrieMap<A, K, V>.HashTrieNode> next = iter.next();
                return new AbstractMap.SimpleEntry<>(next.key, next.value);
            }
        }
    }

    /**
     * Insert a key-value pair into the trie.
     * If the key is new, the size of the trie is increased by 1
     *
     * @param key
     *            The key to insert
     * @param value
     *              The value to insert
     * @throws IllegalArgumentException if key or value is null
     * @return The previous value associated with the key, or null if the key is new
     */
    @Override
    public V insert(K key, V value) {
        if (key == null || value == null)
            throw new IllegalArgumentException("Null key/value");

        // Check if the key is in the trie
        HashTrieNode temp = (HashTrieNode) root;
        for (A alph : key) {
            if (temp.pointers.find(alph) == null) {
                temp.pointers.insert(alph, new HashTrieNode());
            }
            temp = temp.pointers.find(alph);
        }

        V previous = temp.value;
        temp.value = value;
        // New key, increase the size
        if (previous == null) {
            size++;
        }
        return previous;
    }

    public HashTrieMap(Class<K> KClass) {
        super(KClass);
        this.root = new HashTrieNode();
    }

    /**
     * Find the value associated with the key
     *
     * @param key
     *            The key to find
     * @throws IllegalArgumentException if key is null
     * @return The value associated with the key, or null if the key is not found
     */
    @Override
    public V find(K key) {
        if (key == null)
            throw new IllegalArgumentException();

        HashTrieNode temp = (HashTrieNode) root;
        for (A alph : key) {
            // Check for each alph
            if (temp.pointers.find(alph) == null) {
                return null;
            }
            temp = temp.pointers.find(alph);
        }

        return temp.value;
    }

    /**
     * Check if the trie contains a key with the given prefix
     * @param key
     *            The prefix of a key whose presence in this map is to be tested
     * @return true if the trie contains a key with the given prefix
     */
    @Override
    public boolean findPrefix(K key) {
        if (key == null)
            throw new IllegalArgumentException();
        if (isEmpty())
            return false;

        HashTrieNode temp = (HashTrieNode) root;
        for (A alph : key) {
            // Check for each alph
            if (temp.pointers.find(alph) == null) {
                return false;
            }
            temp = temp.pointers.find(alph);
        }

        return true;
    }

    /**
     * Not supported
     */
    @Override
    public void delete(K key) {
//        if (key == null)
//            throw new IllegalArgumentException();
//        if (size == 1)
//            throw new UnsupportedOperationException();
//
//        HashTrieNode result = delete((HashTrieNode) root, key.iterator());
//        if (result == null) {
//            root = new HashTrieNode();
//        } else {
//            root = result;
//        }
        throw new UnsupportedOperationException();
    }

//    private HashTrieNode delete(HashTrieNode node, Iterator<A> iter) {
//        if (node == null) {
//            return null;
//        }
//
//        if (!iter.hasNext()) { // End of the key
//            if (node.value != null) {
//                node.value = null;
//                size--;
//            }
//        } else {
//            A alph = iter.next();
//            if (node.pointers.containsKey(alph)) {
//                node.pointers.put(alph, delete(node.pointers.get(alph), iter));
//                if (node.pointers.get(alph) == null) {
//                    node.pointers.remove(alph);
//                }
//            }
//        }
//
//        if (node.value != null || !node.pointers.isEmpty()) {
//            return node;
//        }
//
//        return null;
//    }

    /**
     * Not supported
     */
    @Override
    public void clear() {
//        if (size == 1)
//            throw new UnsupportedOperationException();
//
//        this.root = new HashTrieNode();
//        size = 0;
        throw new UnsupportedOperationException();
    }
}
