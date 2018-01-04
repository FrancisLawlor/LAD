package adt.impl;

import peer.core.ActorMessage;
import peer.core.ActorMessageType;

public abstract class DistributedMapRequest extends ActorMessage {
    private int requestNum;
    private Object key;
    
    public DistributedMapRequest(int requestNum, Object key, ActorMessageType type) {
        super(type);
        this.requestNum = requestNum;
        this.key = key;
    }
    
    public int getRequestNum() {
        return this.requestNum;
    }
    
    public Object getKey() {
        return this.key;
    }
}
