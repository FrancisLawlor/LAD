package adt.impl;

import java.lang.reflect.Array;

import akka.actor.ActorRef;
import peer.core.PeerToPeerActor;
import peer.core.PeerToPeerActorInit;

public class DistributedHashMapBucket extends PeerToPeerActor {
    private ActorRef owner;
    private Class<?> kClass;
    private Class<?> vClass;
    private int bucketNum;
    private int bucketSize;
    private Object[] bucketArray;
    private int entryCount;
    
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
            DistributedMapAdditionRequest additionRequest = (DistributedMapAdditionRequest) message;
            this.processAdditionRequest(additionRequest);
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
        this.entryCount = 0;
    }
    
    /**
     * 
     * @param additionRequest
     */
    protected void processAdditionRequest(DistributedMapAdditionRequest additionRequest) {
        
    }
    
    /**
     * 
     * @param containsRequest
     */
    protected void processContainsRequest(DistributedMapContainsRequest containsRequest) {
        
    }
    
    /**
     * 
     * @param getRequest
     */
    protected void processGetRequest(DistributedMapGetRequest getRequest) {
        
    }
    
    /**
     * 
     * @param removeRequest
     */
    protected void processRemoveRequest(DistributedMapRemoveRequest removeRequest) {
        
    }
    
    /**
     * Checks for equality between two keys
     * Handles conversion of Object to the appropriate key class
     * @param keyA
     * @param keyB
     * @return
     */
    private boolean equals(Object keyA, Object keyB) {
        boolean equals = (this.kClass.getClass().cast(keyA)).equals(this.kClass.getClass().cast(keyB));
        return equals;
    }
    
    private void refactor() {
        
    }
}
