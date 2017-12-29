package adt.impl;

/**
 * Superclass for Distributed Map Requests
 *
 */
public class DistributedMapRequest {
    private int index;
    private Object k;
    
    public DistributedMapRequest(int index, Object k) {
        this.index = index;
        this.k = k;
    }
    
    public int getIndex() {
        return this.index;
    }
    
    public Object getKey() {
        return this.k;
    }
}
