package adt.impl;

import peer.core.ActorMessage;
import peer.core.ActorMessageType;

public abstract class DistributedMapRequest extends ActorMessage {
    private Object key;
    
    public DistributedMapRequest(Object key, ActorMessageType type) {
        super(type);
        this.key = key;
    }
    
    public Object getKey() {
        return this.key;
    }
}
