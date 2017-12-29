package adt.impl;

import peer.core.ActorMessageType;

public class DistributedMapRemoveRequest extends DistributedMapRequest {
    
    public DistributedMapRemoveRequest(int index, Object k) {
        super(index, k, ActorMessageType.DistributedMapRemoveRequest);
    }
}
