package datastructures.dictionaries;

import cse332.interfaces.trie.TrieMap;
import cse332.types.BString;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * See cse332/interfaces/trie/TrieMap.java
 * and cse332/interfaces/misc/Dictionary.java
 * for method specifications.
 */
public class HashTrieMap<A extends Comparable<A>, K extends BString<A>, V> extends TrieMap<A, K, V> {
    public class HashTrieNode extends TrieNode<Map<A, HashTrieNode>, HashTrieNode> {
        public HashTrieNode() {
            this(null);
        }

        public HashTrieNode(V value) {
            this.pointers = new HashMap<>();
            this.value = value;
        }

        @Override
        public Iterator<Entry<A, HashTrieMap<A, K, V>.HashTrieNode>> iterator() {
            return pointers.entrySet().iterator();
        }
    }

    public HashTrieMap(Class<K> KClass) {
        super(KClass);
        this.root = new HashTrieNode();
    }

    @Override
    public V insert(K key, V value) {
        if (key == null || value == null)
            throw new IllegalArgumentException();

        // Check if the key is in the trie
        HashTrieNode temp = (HashTrieNode) root;
        for (A alph : key) {
            if (!temp.pointers.containsKey(alph)) {
                temp.pointers.put(alph, new HashTrieNode());
            }
            temp = temp.pointers.get(alph);
        }

        V previous = temp.value;
        temp.value = value;
        // New key, increase the size
        if (previous == null) {
            size++;
        }

        return previous;
    }

    @Override
    public V find(K key) {
        if (key == null)
            throw new IllegalArgumentException();

        HashTrieNode temp = (HashTrieNode) root;
        for (A alph : key) {
            // Check for each alph
            if (!temp.pointers.containsKey(alph)) {
                return null;
            }
            temp = temp.pointers.get(alph);
        }

        return temp.value;
    }

    @Override
    public boolean findPrefix(K key) {
        if (key == null)
            throw new IllegalArgumentException();

        HashTrieNode temp = (HashTrieNode) root;
        for (A alph : key) {
            // Check for each alph
            if (!temp.pointers.containsKey(alph)) {
                return false;
            }
            temp = temp.pointers.get(alph);
        }

        return true;
    }

    @Override
    public void delete(K key) {
        if (key == null)
            throw new IllegalArgumentException();

        root = delete((HashTrieNode) root, key.iterator());
    }

    private HashTrieNode delete(HashTrieNode node, Iterator<A> iter) {
        if (node == null) {
            return null;
        }

        if (!iter.hasNext()) { // End of the key
            if (node.value != null) {
                node.value = null;
                size--;
            }
        } else {
            A alph = iter.next();
            if (node.pointers.containsKey(alph)) {
                node.pointers.put(alph, delete(node.pointers.get(alph), iter));
                if (node.pointers.get(alph) == null) {
                    node.pointers.remove(alph);
                }
            }
        }

        if (node.value != null || !node.pointers.isEmpty()) {
            return node;
        }

        return null;
    }

    @Override
    public void clear() {
        this.root = new HashTrieNode();
        size = 0;
    }
}
