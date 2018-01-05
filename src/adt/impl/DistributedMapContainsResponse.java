package adt.impl;

import peer.core.ActorMessageType;

/**
 * Responses with whether the Distributed Map contains a given key
 *
 */
public class DistributedMapContainsResponse extends DistributedMapResponse {
    private boolean contains;
    
    public DistributedMapContainsResponse(int requestNum, int bucketNum, boolean success, Object k, boolean contains) {
        super(requestNum, bucketNum, success, k, ActorMessageType.DistributedMapContainsResponse);
        this.contains = contains;
    }
    
    public boolean contains() {
        return this.contains;
    }
}
