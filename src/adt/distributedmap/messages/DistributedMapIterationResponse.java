package adt.distributedmap.messages;

import peer.frame.core.ActorMessageType;

/**
 * Returns a key-value pair as part of the iteration through the Distributed Map
 *
 */
public class DistributedMapIterationResponse extends DistributedMapResponse {
    private Object v;
    
    public DistributedMapIterationResponse(int requestNum, int bucketNum, Object k, Object v) {
        super(requestNum, bucketNum, true, k, ActorMessageType.DistributedMapIterationResponse);
        this.v = v;
    }
    
    public Object getValue() {
        return this.v;
    }
}
