package adt.frame;

import adt.impl.DistributedMapAdditionResponse;
import adt.impl.DistributedMapContainsResponse;
import adt.impl.DistributedMapGetResponse;
import adt.impl.DistributedMapRefactorGetResponse;
import adt.impl.DistributedMapRemoveResponse;

public interface DistributedMap<K, V> {
    
    void requestAdd(K k, V v);
    
    void requestContains(K k);
    
    void requestGet(K k);
    
    void requestRemove(K k);
    
    void requestRefactor();
    
    void setRefactorGetResponse(DistributedMapRefactorGetResponse response);
    
    int size();
    
    int capacity();
    
    public K getAddKey(DistributedMapAdditionResponse response);
    
    public V getAddValue(DistributedMapAdditionResponse response);
    
    public boolean getAdditionSuccessful(DistributedMapAdditionResponse response);
    
    public K getContainsKey(DistributedMapContainsResponse response);
    
    public boolean getContains(DistributedMapContainsResponse response);
    
    public K getGetKey(DistributedMapGetResponse response);
    
    public V getGetValue(DistributedMapGetResponse response);
    
    public K getRemoveKey(DistributedMapRemoveResponse response);
    
    public V getRemoveValue(DistributedMapRemoveResponse response);
}
