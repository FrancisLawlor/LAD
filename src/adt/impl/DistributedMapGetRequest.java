package adt.impl;

import peer.core.ActorMessageType;

/**
 * Request a value from a key value pair be returned from the DistributedHashMappor
 *
 */
public class DistributedMapGetRequest extends DistributedMapRequest {
    
    public DistributedMapGetRequest(int requestNum, Object k) {
        super(requestNum, k, ActorMessageType.DistributedMapGetRequest);
    }
    
}
