package adt.impl;

/**
 * Responds with the value of a key that has been removed from a Distributed Map
 *
 */
public class DistributedMapRemoveResponse extends DistributedMapResponse {
    private Object v;
    
    public DistributedMapRemoveResponse(Object k, Object v) {
        super(k);
        this.v = v;
    }
    
    public Object getValue() {
        return this.v;
    }
}
