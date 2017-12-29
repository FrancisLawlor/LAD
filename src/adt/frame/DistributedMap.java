package adt.frame;

public interface DistributedMap<K, V> {
    
    void add(K k, V v);
    
    boolean contains(K k);
    
    V remove(K k);
    
}
