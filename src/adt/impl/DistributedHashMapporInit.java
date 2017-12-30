package adt.impl;

import peer.core.ActorMessage;
import peer.core.ActorMessageType;

/**
 * Initialises the DistributedHashMappor
 *
 */
public class DistributedHashMapporInit extends ActorMessage {
    private int bucketSize;
    private int bucketCount;
    private Class<?> kClass;
    
    public DistributedHashMapporInit(int bucketSize, int bucketCount, Class<?> kClass) {
        super(ActorMessageType.DistributedHashMapporInit);
        this.bucketSize = bucketSize;
        this.bucketCount = bucketCount;
        this.kClass = kClass;
    }
    
    public int getBucketSize() {
        return this.bucketSize;
    }
    
    public int getBucketCount() {
        return this.bucketCount;
    }
    
    public Class<?> getKClass() {
        return this.kClass;
    }
}
