package adt.impl;

import peer.core.ActorMessage;
import peer.core.ActorMessageType;

/**
 * A bucket has become full, refactor needed
 *
 */
public class BucketFullRefactorRequest extends ActorMessage {
    private int bucketNum;
    
    public BucketFullRefactorRequest(int bucketNum) {
        super(ActorMessageType.BucketFullRefactorRequest);
        this.bucketNum = bucketNum;
    }
    
    public int getBucketNum() {
        return this.bucketNum;
    }
}
