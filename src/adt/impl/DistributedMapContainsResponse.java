package adt.impl;

import peer.core.ActorMessageType;

/**
 * Responses with whether the Distributed Map contains a given key
 *
 */
public class DistributedMapContainsResponse extends DistributedMapResponse {
    private boolean contains;
    
    public DistributedMapContainsResponse(Object k, boolean contains) {
        super(k, ActorMessageType.DistributedMapContainsResponse);
        this.contains = contains;
    }
    
    public boolean contains() {
        return this.contains;
    }
}
