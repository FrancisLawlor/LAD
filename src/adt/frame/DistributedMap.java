package adt.frame;

public interface DistributedMap<K, V> {
    
    void requestAdd(K k, V v);
    
    void requestContains(K k);
    
    void requestGet(K k);
    
    void requestRemove(K k);
    
}
