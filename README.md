# Data Structures Implementation

Java implementations of **Hash Table** and **Binary Search Tree (BST)** data structures.

---

## Data Structures

### Hash Table (`MyHashTable<K, V>`)

A hash table implementation featuring:

- **Chaining** for collision resolution
- Core Methods:
  - `put(K key, V value)`
  - `get(K key)`
  - `remove(K key)`
  - `contains(K key)`
  - `getKey(V value)`

---

### Binary Search Tree (`BST<K extends Comparable<K>, V>`)

A non-recursive BST implementation with:

- Core Methods:
  - `put(K key, V value)`
  - `get(K key)`
  - `delete(K key)`
  - `size()`
- In-order traversal **iterator** with access to both keys and values.

---

