package adt.impl;

import peer.core.ActorMessageType;

/**
 * Requests a bucket to add a key value pair at this index
 *
 */
public class DistributedMapBucketAdditionRequest extends DistributedMapBucketRequest {
    private Object v;
    
    public DistributedMapBucketAdditionRequest(int requestNum, int index, Object k, Object v) {
        super(requestNum, index, k, ActorMessageType.DistributedMapBucketAdditionRequest);
        this.v = v;
    }
    
    public Object getValue() {
        return this.v;
    }
}
