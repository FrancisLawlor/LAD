package adt.impl;

import java.lang.reflect.Array;

import peer.core.PeerToPeerActor;
import peer.core.PeerToPeerActorInit;

public class DistributedHashMapBucket extends PeerToPeerActor {
    private Class<?> kClass;
    private Class<?> vClass;
    private int bucketNum;
    private int bucketSize;
    private Object[] bucketArray;
    
    /**
     * Actor message processing
     */
    @Override
    public void onReceive(Object message) {
        if (message instanceof PeerToPeerActorInit) {
            PeerToPeerActorInit init = (PeerToPeerActorInit) message;
            this.initialisePeerToPeerActor(init);
        }
        else if (message instanceof DistributedHashMapBucketInit) {
            DistributedHashMapBucketInit init = (DistributedHashMapBucketInit) message;
            this.initialise(init);
        }
        else if (message instanceof DistributedMapAdditionRequest) {
            DistributedMapAdditionRequest addition = (DistributedMapAdditionRequest) message;
            this.processAddition(addition);
        }
    }
    
    /**
     * Initialise the Distributed Hash Map Bucket
     * @param init
     */
    protected void initialise(DistributedHashMapBucketInit init) {
        this.kClass = init.getKeyClass();
        this.vClass = init.getValueClass();
        this.bucketNum = init.getBucketNum();
        this.bucketSize = init.getBucketSize();
        this.bucketArray = (Object[]) Array.newInstance(Object.class, this.bucketSize);
    }
    
    protected void processAddition(DistributedMapAdditionRequest addition) {
        
    }
}
