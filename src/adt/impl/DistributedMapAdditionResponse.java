package adt.impl;

/**
 * Responds with whether an addition of a key value pair to a Distributed Map was successful or not
 *
 */
public class DistributedMapAdditionResponse extends DistributedMapResponse {
    private Object v;
    private boolean successfulAddition;
    
    public DistributedMapAdditionResponse(Object k, Object v, boolean successfulAddition) {
        super(k);
        this.v = v;
        this.successfulAddition = successfulAddition;
    }
    
    public Object getValue() {
        return this.v;
    }
    
    public boolean isAdditionSuccessful() {
        return this.successfulAddition;
    }
}
