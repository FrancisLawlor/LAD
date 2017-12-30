package adt.impl;

import peer.core.ActorMessageType;

public class DistributedMapBucketRemoveRequest extends DistributedMapBucketRequest {
    
    public DistributedMapBucketRemoveRequest(int index, Object k) {
        super(index, k, ActorMessageType.DistributedMapBucketRemoveRequest);
    }
}
