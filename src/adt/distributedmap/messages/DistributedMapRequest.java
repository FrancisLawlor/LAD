package adt.distributedmap.messages;

import peer.frame.core.ActorMessageType;
import peer.frame.messages.ActorMessage;

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
