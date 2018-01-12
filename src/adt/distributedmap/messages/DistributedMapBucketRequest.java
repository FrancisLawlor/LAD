package adt.distributedmap.messages;

import peer.frame.core.ActorMessageType;
import peer.frame.messages.ActorMessage;

/**
 * Superclass for Distributed Map Requests
 *
 */
public abstract class DistributedMapBucketRequest extends ActorMessage {
    private int requestNum;
    private int index;
    private Object k;
    
    public DistributedMapBucketRequest(int requestNum, int index, Object k, ActorMessageType type) {
        super(type);
        this.requestNum = requestNum;
        this.index = index;
        this.k = k;
    }
    
    public int getRequestNum() {
        return this.requestNum;
    }
    
    public int getIndex() {
        return this.index;
    }
    
    public Object getKey() {
        return this.k;
    }
}
