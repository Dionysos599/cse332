package datastructures.dictionaries;

import cse332.datastructures.trees.BinarySearchTree;

/**
 * AVLTree must be a subclass of BinarySearchTree<E> and must use
 * inheritance and calls to superclass methods to avoid unnecessary
 * duplication or copying of functionality.
 * <ol>
 *  <li>Create a subclass of BSTNode, perhaps named AVLNode.</li>
 *  <li>Override the insert method such that it creates AVLNode instances</li>
 *  instead of BSTNode instances.
 *  <li>Do NOT "replace" the children array in BSTNode with a new
 *  children array or left and right fields in AVLNode.  This will
 *  instead mask the super-class fields (i.e., the resulting node
 *  would actually have multiple copies of the node fields, with
 *  code accessing one pair or the other depending on the type of
 *  the references used to access the instance).  Such masking will
 *  lead to highly perplexing and erroneous behavior. Instead,
 *  continue using the existing BSTNode children array.</li>
 *  <li>Ensure that the class does not have redundant methods</li>
 *  <li>Cast a BSTNode to an AVLNode whenever necessary in your AVLTree.
 *  This will result a lot of casts, so we recommend you make private methods
 *  that encapsulate those casts.</li>
 *  <li>Do NOT override the toString method. It is used for grading.</li>
 *  <li>The internal structure of your AVLTree (from this.root to the leaves) must be correct</li>
 * </ol>
 */
public class AVLTree<K extends Comparable<? super K>, V> extends BinarySearchTree<K, V> {

    private class AVLNode extends BSTNode {
        public int height;

        public AVLNode(K key, V value) {
            super(key, value);
            this.height = 0;
        }
    }

    @Override
    public V insert(K key, V value) {
        if (key == null || value == null) {
            throw new IllegalArgumentException("Key or value cannot be null");
        }
        this.root = insert((AVLNode) this.root, key, value);
        return value;
    }

    private AVLNode insert(AVLNode root, K key, V value) {
        if (root == null) {
            size++;
            return new AVLNode(key, value);
        }

        int cmp = key.compareTo(root.key);
        if (cmp < 0) {
            root.children[0] = insert((AVLNode) root.children[0], key, value);
        } else if (cmp > 0) {
            root.children[1] = insert((AVLNode) root.children[1], key, value);
        } else {
            root.value = value;
            return root;
        }

        root.height = 1 + Math.max(height((AVLNode) root.children[0]), height((AVLNode) root.children[1]));

        int balance = height((AVLNode) root.children[0]) - height((AVLNode) root.children[1]);

        if (balance > 1) {
            if (key.compareTo((root.children[0]).key) < 0) {
                return rotateWithLeft(root);
            } else {
                return doubleRotateWithLeft(root);
            }
        } else if (balance < -1) {
            if (key.compareTo((root.children[1]).key) > 0) {
                return rotateWithRight(root);
            } else {
                return doubleRotateWithRight(root);
            }
        }

        return root;
    }

    private int height(AVLNode root) {
        if (root == null) {
            return -1;
        }
        return root.height;
    }

    private AVLNode rotateWithLeft(AVLNode root) {
        AVLNode temp = (AVLNode) root.children[0];
        root.children[0] = temp.children[1];
        temp.children[1] = root;

        root.height = Math.max(height((AVLNode) root.children[0]), height((AVLNode) root.children[1])) + 1;
        temp.height = Math.max(height((AVLNode) temp.children[0]), height((AVLNode) temp.children[1])) + 1;

        return temp;
    }

    private AVLNode rotateWithRight(AVLNode root) {
        AVLNode temp = (AVLNode) root.children[1];
        root.children[1] = temp.children[0];
        temp.children[0] = root;

        root.height = Math.max(height((AVLNode) root.children[0]), height((AVLNode) root.children[1])) + 1;
        temp.height = Math.max(height((AVLNode) temp.children[0]), height((AVLNode) temp.children[1])) + 1;

        return temp;
    }

    private AVLNode doubleRotateWithLeft(AVLNode root) {
        root.children[0] = rotateWithRight((AVLNode) root.children[0]);
        return rotateWithLeft(root);
    }

    private AVLNode doubleRotateWithRight(AVLNode root) {
        root.children[1] = rotateWithLeft((AVLNode) root.children[1]);
        return rotateWithRight(root);
    }
}
