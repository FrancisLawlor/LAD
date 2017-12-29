package adt.impl;

import peer.core.ActorMessageType;

public class DistributedMapGetRequest extends DistributedMapRequest {
    
    public DistributedMapGetRequest(int index, Object k) {
        super(index, k, ActorMessageType.DistributedMapGetRequest);
    }
}
