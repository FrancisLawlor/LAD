package adt.impl;

import peer.core.ActorMessageType;

/**
 * Requests a bucket to add a key value pair at this index
 *
 */
public class DistributedMapBucketAdditionRequest extends DistributedMapBucketRequest {
    private Object v;
    
    
    public DistributedMapBucketAdditionRequest(int index, Object k, Object v) {
        super(index, k, ActorMessageType.DistributedMapBucketAdditionRequest);
        this.v = v;
    }
    
    public Object getValue() {
        return this.v;
    }
}
