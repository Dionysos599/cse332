package datastructures.dictionaries;

import cse332.datastructures.trees.BinarySearchTree;

import java.lang.reflect.Array;

/**
 * AVLTree must be a subclass of BinarySearchTree<E> and must use
 * inheritance and calls to superclass methods to avoid unnecessary
 * duplication or copying of functionality.
 * <ol>
 * <li>Create a subclass of BSTNode, perhaps named AVLNode.</li>
 * <li>Override the insert method such that it creates AVLNode instances</li>
 * instead of BSTNode instances.
 * <li>Do NOT "replace" the children array in BSTNode with a new
 * children array or left and right fields in AVLNode.  This will
 * instead mask the super-class fields (i.e., the resulting node
 * would actually have multiple copies of the node fields, with
 * code accessing one pair or the other depending on the type of
 * the references used to access the instance).  Such masking will
 * lead to highly perplexing and erroneous behavior. Instead,
 * continue using the existing BSTNode children array.</li>
 * <li>Ensure that the class does not have redundant methods</li>
 * <li>Cast a BSTNode to an AVLNode whenever necessary in your AVLTree.
 * This will result a lot of casts, so we recommend you make private methods
 * that encapsulate those casts.</li>
 * <li>Do NOT override the toString method. It is used for grading.</li>
 * <li>The internal structure of your AVLTree (from this.root to the leaves) must be correct</li>
 * </ol>
 */
public class AVLTree<K extends Comparable<? super K>, V> extends BinarySearchTree<K, V> {

    private AVLNode root;

    public AVLTree() {
        super();
        this.root = null;
    }

    public class AVLNode extends BSTNode {
        public int height;

        public AVLNode(K key, V value) {
            super(key, value);
            this.height = 0;
        }
    }

    private void rotateWithLeft(AVLNode root) {
        AVLNode temp = (AVLNode)root.children[0];
        root.children[0] = temp.children[1];
        temp.children[1] = root;

        root.height = Math.max(((AVLNode)root.children[0]).height,
                               ((AVLNode)root.children[1]).height) + 1;
        temp.height = Math.max(((AVLNode)temp.children[0]).height,
                               ((AVLNode)temp.children[1]).height) + 1;
        root = temp;
    }

    private void rotateWithRight(AVLNode root) {
        AVLNode temp = (AVLNode)root.children[1];
        root.children[1] = temp.children[0];
        temp.children[0] = root;

        root.height = Math.max(((AVLNode)root.children[0]).height,
                               ((AVLNode)root.children[1]).height) + 1;
        temp.height = Math.max(((AVLNode)temp.children[0]).height,
                               ((AVLNode)temp.children[1]).height) + 1;
        root = temp;
    }

    private void doubleRotateWithLeft(AVLNode root) {
        rotateWithRight((AVLNode)root.children[0]);
        rotateWithLeft(root);
    }

    private void doubleRotateWithRight(AVLNode root) {
        rotateWithLeft((AVLNode)root.children[1]);
        rotateWithRight(root);
    }

    @Override
    public V insert(K key, V value) {
        V oldValue = value;
        this.root = insert(this.root, key, value);
        return oldValue;
    }

    private AVLNode insert(AVLNode root, K key, V value) {
        if (root == null) {
            return new AVLNode(key, value);
        }

        // insert
        int compare = key.compareTo(root.key);
        if (compare < 0) {
            root.children[0] = insert((AVLNode)root.children[0], key, value);
        } else if (compare > 0) {
            root.children[1] = insert((AVLNode)root.children[1], key, value);
        } else {
            root.value = value;
            return root;
        }

        root.height = Math.max(((AVLNode)root.children[0]).height,
                               ((AVLNode)root.children[1]).height) + 1;

        int diff = ((AVLNode)root.children[0]).height - ((AVLNode)root.children[1]).height;
        if (diff > 1) { // left more
            if (key.compareTo(root.children[0].key) < 0) { // left of left
                rotateWithLeft(root);
            } else { // right of left
                doubleRotateWithLeft(root);
            }
        } else if (diff < -1) { // right more
            if (key.compareTo(root.children[1].key) > 0) { // right of right
                rotateWithRight(root);
            } else { // left of right
                doubleRotateWithRight(root);
            }
        }

        return root;
    }

}
