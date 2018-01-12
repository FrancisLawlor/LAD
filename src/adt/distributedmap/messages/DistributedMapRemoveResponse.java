package adt.distributedmap.messages;

import peer.frame.core.ActorMessageType;

/**
 * Responds with the value of a key that has been removed from a Distributed Map
 *
 */
public class DistributedMapRemoveResponse extends DistributedMapResponse {
    private Object v;
    
    public DistributedMapRemoveResponse(int requestNum, int bucketNum, boolean success, Object k, Object v) {
        super(requestNum, bucketNum, success, k, ActorMessageType.DistributedMapRemoveResponse);
        this.v = v;
    }
    
    public Object getValue() {
        return this.v;
    }
}
