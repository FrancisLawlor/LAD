package adt.impl;

import peer.core.ActorMessageType;

/**
 * Returns a key-value pair as part of the iteration through the Distributed Map
 *
 */
public class DistributedMapIterationResponse extends DistributedMapResponse {
    private Object v;
    
    public DistributedMapIterationResponse(int bucketNum, Object k, Object v) {
        super(bucketNum, true, k, ActorMessageType.DistributedMapIterationResponse);
        this.v = v;
    }
    
    public Object getValue() {
        return this.v;
    }
}
