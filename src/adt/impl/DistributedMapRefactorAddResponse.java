package adt.impl;

import peer.core.ActorMessageType;

public class DistributedMapRefactorAddResponse extends DistributedMapResponse {
    private Object v;
    
    public DistributedMapRefactorAddResponse(int bucketNum, boolean success, Object k, Object v) {
        super(bucketNum, success, k, ActorMessageType.DistributedMapRefactorAddResponse);
    }
    
    public Object getValue() {
        return this.v;
    }
}
