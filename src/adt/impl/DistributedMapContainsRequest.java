package adt.impl;

import peer.core.ActorMessageType;

/**
 * Ask if a key is contained by a DistributedHashMappor
 *
 */
public class DistributedMapContainsRequest extends DistributedMapRequest {
    
    public DistributedMapContainsRequest(Object k) {
        super(k, ActorMessageType.DistributedMapContainsRequest);
    }
    
}
