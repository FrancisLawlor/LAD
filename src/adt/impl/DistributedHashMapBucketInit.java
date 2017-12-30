package adt.impl;

import akka.actor.ActorRef;
import peer.core.ActorMessage;
import peer.core.ActorMessageType;

/**
 * Initialises the Distributed Hash Map Bucket
 *
 */
public class DistributedHashMapBucketInit extends ActorMessage {
    private ActorRef owner;
    private int bucketNum;
    private int bucketSize;
    private Class<?> kClass;
    
    public DistributedHashMapBucketInit(ActorRef owner, int bucketNum, int bucketSize, Class<?> kClass) {
        super(ActorMessageType.DistributedHashMapBucketInit);
        this.owner = owner;
        this.bucketNum = bucketNum;
        this.bucketSize = bucketSize;
        this.kClass = kClass;
    }
    
    public ActorRef getOwner() {
        return this.owner;
    }
    
    public int getBucketNum() {
        return this.bucketNum;
    }
    
    public int getBucketSize() {
        return this.bucketSize;
    }
    
    public Class<?> getKeyClass() {
        return this.kClass;
    }
}
