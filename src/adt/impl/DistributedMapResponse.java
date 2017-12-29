package adt.impl;

/**
 * Superclass for Distributed Map Responses
 *
 */
public class DistributedMapResponse {
    private Object k;
    
    public DistributedMapResponse(Object k) {
        this.k = k;
    }
    
    public Object getKey() {
        return this.k;
    }
}
