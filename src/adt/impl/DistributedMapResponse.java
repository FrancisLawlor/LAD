package adt.impl;

import peer.core.ActorMessage;
import peer.core.ActorMessageType;

/**
 * Superclass for Distributed Map Responses
 *
 */
public abstract class DistributedMapResponse extends ActorMessage {
    private Object k;
    
    public DistributedMapResponse(Object k, ActorMessageType type) {
        super(type);
        this.k = k;
    }
    
    public Object getKey() {
        return this.k;
    }
}
