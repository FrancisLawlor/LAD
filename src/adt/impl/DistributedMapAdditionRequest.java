package adt.impl;

import peer.core.ActorMessageType;

/**
 * Request an addition to a DistributedHashMappor
 *
 */
public class DistributedMapAdditionRequest extends DistributedMapRequest {
    private Object v;
    
    public DistributedMapAdditionRequest(Object k, Object v) {
        super(k, ActorMessageType.DistributedMapAdditionRequest);
        this.v = v;
    }
    
    public Object getValue() {
        return this.v;
    }
}
