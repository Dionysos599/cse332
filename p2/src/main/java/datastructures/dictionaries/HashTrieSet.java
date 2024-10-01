package datastructures.dictionaries;

import cse332.interfaces.trie.TrieSet;
import cse332.types.BString;

public class HashTrieSet<A extends Comparable<A>, E extends BString<A>> extends TrieSet<A, E> {

    public HashTrieSet(Class<E> Type) {
        super(new HashTrieMap<>(Type));
    }

}
