import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;

public class BST<K extends Comparable<K>, V> {
    private Node root;

    private class Node {
        private K key;
        private V val;
        private Node left, right;

        public Node(K key, V val) {
            this.key = key;
            this.val = val;
        }
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
                // Key already exists, update value
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
    }

    public Iterable<K> iterator() {
        return new Iterable<K>() {
            @Override
            public Iterator<K> iterator() {
                return new BSTIterator();
            }
        };
    }

    private class BSTIterator implements Iterator<K> {
        private ArrayList<K> keys;
        private int index;

        public BSTIterator() {
            keys = new ArrayList<>();

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
                    keys.add(current.key);

                    // Move to the right subtree
                    current = current.right;
                }
            }

            index = 0;
        }

        @Override
        public boolean hasNext() {
            return index < keys.size();
        }

        @Override
        public K next() {
            if (!hasNext()) throw new NoSuchElementException();
            return keys.get(index++);
        }
    }
}