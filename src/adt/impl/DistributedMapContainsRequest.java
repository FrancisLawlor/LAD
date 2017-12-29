package adt.impl;

import peer.core.ActorMessageType;

/**
 * Requests a bucket to check if it contains a key
 *
 */
public class DistributedMapContainsRequest extends DistributedMapRequest {
    
    public DistributedMapContainsRequest(int index, Object k) {
        super(index, k, ActorMessageType.DistributedMapContainsRequest);
    }
}
