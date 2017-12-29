package adt.impl;

/**
 * Requests a bucket to add a key value pair at this index
 *
 */
public class DistributedMapAdditionRequest extends DistributedMapRequest {
    private Object v;
    
    
    public DistributedMapAdditionRequest(int index, Object k, Object v) {
        super(index, k);
        this.v = v;
    }
    
    public Object getValue() {
        return this.v;
    }
}
