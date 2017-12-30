package adt.impl;

import peer.core.ActorMessage;
import peer.core.ActorMessageType;

/**
 * Superclass for Distributed Map Responses
 *
 */
public abstract class DistributedMapResponse extends ActorMessage {
    private int bucketNum;
    private boolean success;
    private Object k;
    
    public DistributedMapResponse(int bucketNum, boolean success, Object k, ActorMessageType type) {
        super(type);
        this.bucketNum = bucketNum;
        this.success = success;
        this.k = k;
    }
    
    public int getBucketNum() {
        return this.bucketNum;
    }
    
    public boolean getSuccess() {
        return this.success;
    }
    
    public Object getKey() {
        return this.k;
    }
}
