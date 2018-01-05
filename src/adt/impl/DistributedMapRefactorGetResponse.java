package adt.impl;

import peer.core.ActorMessageType;

/**
 * Sends back an item from a bucket for refactoring
 *
 */
public class DistributedMapRefactorGetResponse extends DistributedMapResponse {
    private Object v;
    
    public DistributedMapRefactorGetResponse(Object k, Object v) {
        super(-1, -1, false, k, ActorMessageType.DistributedMapRefactorGetResponse);
        this.v = v;
    }
    
    public Object getValue() {
        return this.v;
    }
}
