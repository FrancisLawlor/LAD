package adt.distributedmap.messages;

import peer.frame.core.ActorMessageType;

/**
 * Responds with the gotten value from the Distributed Map
 *
 */
public class DistributedMapGetResponse extends DistributedMapResponse {
    private Object v;
    
    public DistributedMapGetResponse(int requestNum, int bucketNum, boolean success, Object k, Object v) {
        super(requestNum, bucketNum, success, k, ActorMessageType.DistributedMapGetResponse);
        this.v = v;
    }
    
    public Object getValue() {
        return this.v;
    }
}
