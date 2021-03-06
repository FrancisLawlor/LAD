package adt.distributedmap.messages;

import peer.frame.core.ActorMessageType;

public class DistributedMapRefactorAddResponse extends DistributedMapResponse {
    private Object v;
    
    public DistributedMapRefactorAddResponse(int bucketNum, boolean success, Object k, Object v) {
        super(-1, bucketNum, success, k, ActorMessageType.DistributedMapRefactorAddResponse);
    }
    
    public Object getValue() {
        return this.v;
    }
}
