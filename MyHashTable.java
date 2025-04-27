public class MyHashTable<K, V> {
    private class HashNode<K, V> {
        private K key;
        private V value;
        private HashNode<K, V> next;

        public HashNode(K key, V value) {
            this.key = key;
            this.value = value;
        }
        public String toString() {
            return "{" + key + ", " + value + "}";
        }
    }
    private HashNode<K, V>[] chainArray;
    private int M = 11;
    private int size;

    public MyHashTable() {
        chainArray = new HashNode[M];
        size = 0;
    }
    public MyHashTable(int size) {
        this.M = M;
        chainArray = new HashNode[M];
        size = 0;
    }
    private int hash(K key) {
        //basic hash function that works for any key
        int hashcode = key.hashCode();
        return Math.abs(hashcode) % M;
    }
    public void put(K key, V value) {
        int index = hash(key);
        HashNode<K, V> newNode = new HashNode(key, value);

        if(chainArray[index] == null) {
            chainArray[index] = newNode;
            size++;
            return;
        }

        HashNode<K, V> current = chainArray[index];
        HashNode<K, V> previous = null;

        while(current != null) {
            if(current.key.equals(key)) {
                current.value = value;
                return;
            }
            previous = current;
            current = current.next;
        }

        previous.next = newNode;
        size++;
    }

    public V get(K key){
        int index = hash(key);
        HashNode<K, V> current = chainArray[index];

        while(current != null) {
            if(current.key.equals(key)) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    public V remove(K key){
        int index = hash(key);
        HashNode<K, V> current = chainArray[index];
        HashNode<K, V> previous = null;
        // if bucket is empty
        if(current == null) {
            return null;
        }

        // if key found at the head of the chain
        if(current.key.equals(key)) {
            V removedValue = current.value;
            chainArray[index] = current.next;
            size--;
            return removedValue;
        }

        // search for the key
        while(current != null && !current.key.equals(key)) {
            previous = current;
            current = current.next;
        }
        // if key is not found
        if(current == null) {
            return null;
        }

        //remove the node
        V removedValue = current.value;
        previous.next = current.next;
        size--;
        return removedValue;
    }

    public boolean contains(V value){
        for (int i = 0; i < M; i++) {
            HashNode<K, V> current = chainArray[i];
            while(current != null) {
                if(current.value.equals(value)) {
                    return true;
                }
                current = current.next;
            }
        }
        return false;
    }
    public K getKey(V value){
        for (int i = 0; i < M; i++) {
            HashNode<K, V> current = chainArray[i];
            while(current != null) {
                if(current.value.equals(value)) {
                    return current.key;
                }
                current = current.next;
            }
        }
        return null;
    }
}
