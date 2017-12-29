package adt.impl;

/**
 * Responds with the gotten value from the Distributed Map
 *
 */
public class DistributedMapGetResponse extends DistributedMapResponse {
    private Object v;
    
    public DistributedMapGetResponse(Object k, Object v) {
        super(k);
        this.v = v;
    }
    
    public Object getValue() {
        return this.v;
    }
}
