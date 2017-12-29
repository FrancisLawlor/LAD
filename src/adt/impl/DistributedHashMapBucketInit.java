package adt.impl;

/**
 * Initialises the Distributed Hash Map Bucket
 *
 */
public class DistributedHashMapBucketInit {
    private int bucketNum;
    private int bucketSize;
    private Class<?> kClass;
    private Class<?> vClass;
    
    public DistributedHashMapBucketInit(int bucketNum, int bucketSize, Class<?> kClass, Class<?> vClass) {
        this.bucketNum = bucketNum;
        this.bucketSize = bucketSize;
        this.kClass = kClass;
        this.vClass = vClass;
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
    
    public Class<?> getValueClass() {
        return this.vClass;
    }
}
