package adt.impl;

import peer.core.ActorMessageType;

public class DistributedMapBucketGetRequest extends DistributedMapBucketRequest {
    
    public DistributedMapBucketGetRequest(int index, Object k) {
        super(index, k, ActorMessageType.DistributedMapBucketGetRequest);
    }
}
