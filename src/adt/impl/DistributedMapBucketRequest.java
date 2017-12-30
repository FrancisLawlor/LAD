package adt.impl;

import peer.core.ActorMessage;
import peer.core.ActorMessageType;

/**
 * Superclass for Distributed Map Requests
 *
 */
public abstract class DistributedMapBucketRequest extends ActorMessage {
    private int index;
    private Object k;
    
    public DistributedMapBucketRequest(int index, Object k, ActorMessageType type) {
        super(type);
        this.index = index;
        this.k = k;
    }
    
    public int getIndex() {
        return this.index;
    }
    
    public Object getKey() {
        return this.k;
    }
}
