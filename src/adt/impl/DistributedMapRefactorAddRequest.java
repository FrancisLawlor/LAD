package adt.impl;

import peer.core.ActorMessageType;

/**
 * Requests a key value pair be reentered into a new bucket at a new index after refactoring
 *
 */
public class DistributedMapRefactorAddRequest extends DistributedMapBucketRequest {
    private Object v;
    
    public DistributedMapRefactorAddRequest(int index, Object k, Object v) {
        super(-1, index, k, ActorMessageType.DistributedMapRefactorAddRequest);
        this.v = v;
    }
    
    public Object getValue() {
        return this.v;
    }
}
