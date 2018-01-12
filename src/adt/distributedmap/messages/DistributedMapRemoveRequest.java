package adt.distributedmap.messages;

import peer.frame.core.ActorMessageType;

/**
 * Request a key value pair be removed from a DistributedHashMappor
 *
 */
public class DistributedMapRemoveRequest extends DistributedMapRequest {
    
    public DistributedMapRemoveRequest(int requestNum, Object k) {
        super(requestNum, k, ActorMessageType.DistributedMapRemoveRequest);
    }
    
}
