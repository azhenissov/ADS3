import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;

public class BST<K extends Comparable<K>, V> implements Iterable<BST.Entry<K, V>> {
    private Node root;
    private int size;  // Added size field to track number of nodes

    // Static Entry class to hold both key and value for iteration
    public static class Entry<K, V> {
        private final K key;
        private final V value;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }
    }

    private class Node {
        private K key;
        private V val;
        private Node left, right;

        public Node(K key, V val) {
            this.key = key;
            this.val = val;
        }
    }

    // Constructor
    public BST() {
        root = null;
        size = 0;
    }

    // Size method
    public int size() {
        return size;
    }

    // Check if tree is empty
    public boolean isEmpty() {
        return size == 0;
    }

    public void put(K key, V val) {
        if (key == null) throw new IllegalArgumentException("Key cannot be null");
        if (val == null) {
            delete(key);
            return;
        }

        // If tree is empty, create root
        if (root == null) {
            root = new Node(key, val);
            size++;
            return;
        }

        Node current = root;
        Node parent = null;

        while (current != null) {
            parent = current;
            int cmp = key.compareTo(current.key);

            if (cmp < 0) {
                current = current.left;
            } else if (cmp > 0) {
                current = current.right;
            } else {
                // Key already exists, update value without changing size
                current.val = val;
                return;
            }
        }

        // Create new node at proper position
        Node newNode = new Node(key, val);
        int cmp = key.compareTo(parent.key);

        if (cmp < 0) {
            parent.left = newNode;
        } else {
            parent.right = newNode;
        }

        size++; // Increment size after adding new node
    }

    public V get(K key) {
        if (key == null) throw new IllegalArgumentException("Key cannot be null");

        Node current = root;

        while (current != null) {
            int cmp = key.compareTo(current.key);

            if (cmp < 0) {
                current = current.left;
            } else if (cmp > 0) {
                current = current.right;
            } else {
                // Found the key
                return current.val;
            }
        }

        return null; // Key not found
    }

    public void delete(K key) {
        if (key == null) throw new IllegalArgumentException("Key cannot be null");
        if (root == null) return;

        Node parent = null;
        Node current = root;
        boolean isLeftChild = false;

        // Find the node to delete and its parent
        while (current != null && !key.equals(current.key)) {
            parent = current;
            int cmp = key.compareTo(current.key);

            if (cmp < 0) {
                current = current.left;
                isLeftChild = true;
            } else {
                current = current.right;
                isLeftChild = false;
            }
        }

        // If key not found
        if (current == null) return;

        // Case 1: Node has no children
        if (current.left == null && current.right == null) {
            if (current == root) {
                root = null;
            } else if (isLeftChild) {
                parent.left = null;
            } else {
                parent.right = null;
            }
        }
        // Case 2: Node has only right child
        else if (current.left == null) {
            if (current == root) {
                root = current.right;
            } else if (isLeftChild) {
                parent.left = current.right;
            } else {
                parent.right = current.right;
            }
        }
        // Case 3: Node has only left child
        else if (current.right == null) {
            if (current == root) {
                root = current.left;
            } else if (isLeftChild) {
                parent.left = current.left;
            } else {
                parent.right = current.left;
            }
        }
        // Case 4: Node has two children
        else {
            // Find successor (minimum node in right subtree)
            Node successorParent = current;
            Node successor = current.right;

            while (successor.left != null) {
                successorParent = successor;
                successor = successor.left;
            }

            // If successor is not right child of the node to be deleted
            if (successorParent != current) {
                successorParent.left = successor.right;
                successor.right = current.right;
            }

            // Replace node to be deleted with successor
            successor.left = current.left;

            if (current == root) {
                root = successor;
            } else if (isLeftChild) {
                parent.left = successor;
            } else {
                parent.right = successor;
            }
        }

        size--; // Decrease size after removing node
    }

    // Implement Iterable interface - returns iterator for Entry objects
    @Override
    public Iterator<Entry<K, V>> iterator() {
        return new BSTIterator();
    }

    // Iterator that provides access to both key and value
    private class BSTIterator implements Iterator<Entry<K, V>> {
        private ArrayList<Entry<K, V>> entries;
        private int index;

        public BSTIterator() {
            entries = new ArrayList<>();

            // Perform inorder traversal iteratively
            if (root != null) {
                Stack<Node> stack = new Stack<>();
                Node current = root;

                while (current != null || !stack.isEmpty()) {
                    // Reach the leftmost node of the current node
                    while (current != null) {
                        stack.push(current);
                        current = current.left;
                    }

                    // Current is null at this point
                    current = stack.pop();

                    // Add both key and value as Entry
                    entries.add(new Entry<>(current.key, current.val));

                    // Move to the right subtree
                    current = current.right;
                }
            }

            index = 0;
        }

        @Override
        public boolean hasNext() {
            return index < entries.size();
        }

        @Override
        public Entry<K, V> next() {
            if (!hasNext()) throw new NoSuchElementException();
            return entries.get(index++);
        }
    }
}