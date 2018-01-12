package adt.distributedmap.messages;

import peer.frame.core.ActorMessageType;
import peer.frame.messages.ActorMessage;

/**
 * Superclass for Distributed Map Responses
 *
 */
public abstract class DistributedMapResponse extends ActorMessage {
    private int requestNum;
    private int bucketNum;
    private boolean success;
    private Object k;
    
    public DistributedMapResponse(int requestNum, int bucketNum, boolean success, Object k, ActorMessageType type) {
        super(type);
        this.requestNum = requestNum;
        this.bucketNum = bucketNum;
        this.success = success;
        this.k = k;
    }
    
    public int getRequestNum() {
        return this.requestNum;
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
