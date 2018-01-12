package adt.distributedmap.messages;

import peer.frame.core.ActorMessageType;

/**
 * Request an addition to a DistributedHashMappor
 *
 */
public class DistributedMapAdditionRequest extends DistributedMapRequest {
    private Object v;
    
    public DistributedMapAdditionRequest(int requestNum, Object k, Object v) {
        super(requestNum, k, ActorMessageType.DistributedMapAdditionRequest);
        this.v = v;
    }
    
    public Object getValue() {
        return this.v;
    }
}
