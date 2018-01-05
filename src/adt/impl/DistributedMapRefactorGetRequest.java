package adt.impl;

import peer.core.ActorMessage;
import peer.core.ActorMessageType;

/**
 * Asks for all the items in a bucket to be sent back for refactoring
 *
 */
public class DistributedMapRefactorGetRequest extends ActorMessage {
    private int bucketNum;
    
    public DistributedMapRefactorGetRequest(int bucketNum) {
        super(ActorMessageType.DistributedMapRefactorGetResponse);
        this.bucketNum = bucketNum;
    }
    
    public int getBucketNum() {
        return this.bucketNum;
    }
}
