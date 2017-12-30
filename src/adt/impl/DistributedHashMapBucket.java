package adt.impl;

import akka.actor.ActorRef;
import peer.core.PeerToPeerActor;
import peer.core.PeerToPeerActorInit;
import peer.core.xcept.UnknownMessageException;

public class DistributedHashMapBucket extends PeerToPeerActor {
    private ActorRef owner;
    private Class<?> kClass;
    private int bucketNum;
    private int bucketSize;
    private Object[] keyArray;
    private Object[] valueArray;
    private int[] originalIndices;
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
        else if (message instanceof DistributedMapContainsRequest) {
            DistributedMapContainsRequest containsRequest = (DistributedMapContainsRequest) message;
            this.processContainsRequest(containsRequest);
        }
        else if (message instanceof DistributedMapGetRequest) {
            DistributedMapGetRequest getRequest = (DistributedMapGetRequest) message;
            this.processGetRequest(getRequest);
        }
        else if (message instanceof DistributedMapRemoveRequest) {
            DistributedMapRemoveRequest removeRequest = (DistributedMapRemoveRequest) message;
            this.processRemoveRequest(removeRequest);
        }
        else if (message instanceof DistributedMapRefactorGetRequest) {
            DistributedMapRefactorGetRequest refactorGetRequest = (DistributedMapRefactorGetRequest) message;
            this.processRefactorGetRequest(refactorGetRequest);
        }
        else {
            throw new UnknownMessageException();
        }
    }
    
    /**
     * Initialise the Distributed Hash Map Bucket
     * @param init
     */
    protected void initialise(DistributedHashMapBucketInit init) {
        this.owner = init.getOwner();
        this.kClass = init.getKeyClass();
        this.bucketNum = init.getBucketNum();
        this.bucketSize = init.getBucketSize();
        this.keyArray = new Object[this.bucketSize];
        this.valueArray = new Object[this.bucketSize];
        this.originalIndices = new int[this.bucketSize];
        this.entryCount = 0;
        for (int i = 0; i < this.bucketSize; i++) {
            this.originalIndices[i] = -1;
        }
    }
    
    /**
     * Attempts to place it at the bucketIndex if empty
     * Uses linear probing to find another part of the bucket that is empty if bucketIndex is full
     * If no empty space is available then it calls for a refactor and responses with an addition failure response
     * @param additionRequest
     */
    protected void processAdditionRequest(DistributedMapAdditionRequest additionRequest) {
        Object value = null;
        boolean additionSuccessful = false;
        if (this.entryCount < this.bucketSize) {
            int bucketIndex = additionRequest.getIndex() % this.bucketSize;
            Object key = this.keyArray[bucketIndex];
            if (key == null) {
                this.keyArray[bucketIndex] = additionRequest.getKey();
                this.valueArray[bucketIndex] = additionRequest.getValue();
                this.originalIndices[bucketIndex] = bucketIndex;
                this.entryCount++;
            }
            else if (equals(key, additionRequest.getKey())) {
                value = this.valueArray[bucketIndex];
                this.valueArray[bucketIndex] = additionRequest.getValue();
                this.originalIndices[bucketIndex] = bucketIndex;
            }
            else {
                boolean continueProbe = true;
                for (int i = 1; i < this.bucketSize && continueProbe; i++) {
                    int linearProbeIndex = (bucketIndex + i) % this.bucketSize;
                    key = this.keyArray[linearProbeIndex];
                    if (key == null) {
                        this.keyArray[linearProbeIndex] = additionRequest.getKey();
                        this.valueArray[linearProbeIndex] = additionRequest.getValue();
                        this.originalIndices[linearProbeIndex] = bucketIndex;
                        this.entryCount++;
                        continueProbe = false;
                    }
                    else if (equals(key, additionRequest.getKey())) {
                        value = this.valueArray[linearProbeIndex];
                        this.valueArray[linearProbeIndex] = additionRequest.getValue();
                        this.originalIndices[linearProbeIndex] = bucketIndex;
                        continueProbe = false;
                    }
                    else {
                        continueProbe = true;
                    }
                }
            }
            additionSuccessful = true;
        }
        else {
            additionSuccessful = false;
            this.refactor();
        }
        DistributedMapAdditionResponse response = new DistributedMapAdditionResponse(additionRequest.getKey(), value, additionSuccessful);
        this.owner.tell(response, getSelf());
    }
    
    /**
     * Checks bucket if it contains a key
     * If another key is occupying the designated bucketIndex it linearly probes to check if it's contained elsewhere
     * Linearly probes up until an empty entry missing a key value pair
     * Assumes no keys to check exist beyond this empty entry as linear probing in addition would have filled it first
     * @param containsRequest
     */
    protected void processContainsRequest(DistributedMapContainsRequest containsRequest) {
        boolean contains = false;
        int bucketIndex = containsRequest.getIndex() % this.bucketSize;
        Object key = this.keyArray[bucketIndex];
        if (key == null) {
            contains = false;
        }
        else if (equals(key, containsRequest.getKey())) {
            contains = true;
        }
        else {
            boolean continueProbe = true;
            for (int i = 1; i < this.bucketSize && continueProbe; i++) {
                int linearProbeIndex = (bucketIndex + i) % this.bucketSize;
                key = this.keyArray[linearProbeIndex];
                if (key == null) {
                    contains = false;
                    continueProbe = false;
                }
                else if (equals(key, containsRequest.getKey())) {
                    contains = true;
                    continueProbe = false;
                }
                else {
                    continueProbe = true;
                }
            }
        }
        DistributedMapContainsResponse response = new DistributedMapContainsResponse(containsRequest.getKey(), contains);
        this.owner.tell(response, getSelf());
    }
    
    /**
     * Gets a value from a bucket based on a key
     * If another key value pair is occupying the designated bucketIndex it linearly probes to check if it's contained elsewhere
     * Linearly probes up until an empty entry missing a key value pair
     * Assumes no key value pairs to get exist beyond this empty entry as linear probing in addition would have filled it first
     * @param getRequest
     */
    protected void processGetRequest(DistributedMapGetRequest getRequest) {
        Object value = null;
        int bucketIndex = getRequest.getIndex() % this.bucketSize;
        Object key = this.keyArray[bucketIndex];
        if (key == null) {
            value = null;
        }
        else if (equals(key, getRequest.getKey())) {
            value = this.valueArray[bucketIndex];
        }
        else {
            boolean continueProbe = true;
            for (int i = 1; i < this.bucketSize && continueProbe; i++) {
                int linearProbeIndex = (bucketIndex + i) % this.bucketSize;
                key = this.keyArray[linearProbeIndex];
                if (key == null) {
                    continueProbe = false;
                }
                else if (equals(key, getRequest.getKey())) {
                    value = this.valueArray[linearProbeIndex];
                    continueProbe = false;
                }
                else {
                    continueProbe = true;
                }
            }
        }
        DistributedMapGetResponse response = new DistributedMapGetResponse(getRequest.getKey(), value);
        this.owner.tell(response, getSelf());
    }
    
    /**
     * Removes a key value pair that matches this key
     * If another key is occupying the designated bucketIndex it linearly probes to check if it's contained elsewhere
     * Linearly probes up until an empty entry missing a key value pair
     * Assumes no key value pairs to remove exist beyond this empty entry as linear probing in addition would have filled it first
     * @param removeRequest
     */
    protected void processRemoveRequest(DistributedMapRemoveRequest removeRequest) {
        Object value = null;
        int bucketIndex = removeRequest.getIndex() % this.bucketSize;
        Object key = this.keyArray[bucketIndex];
        if (key == null) {
            value = null;
        }
        else if (equals(key, removeRequest.getKey())) {
            value = this.valueArray[bucketIndex];
            this.keyArray[bucketIndex] = null;
            this.valueArray[bucketIndex] = null;
            this.originalIndices[bucketIndex] = -1;
            this.entryCount--;
            this.shiftBack(bucketIndex);
        }
        else {
            boolean continueProbe = true;
            for (int i = 1; i < this.bucketSize && continueProbe; i++) {
                int linearProbeIndex = (bucketIndex + i) % this.bucketSize;
                key = this.keyArray[linearProbeIndex];
                if (key == null) {
                    continueProbe = false;
                }
                else if (equals(key, removeRequest.getKey())) {
                    value = this.valueArray[linearProbeIndex];
                    this.keyArray[linearProbeIndex] = null;
                    this.valueArray[linearProbeIndex] = null;
                    this.originalIndices[linearProbeIndex] = -1;
                    this.entryCount--;
                    this.shiftBack(linearProbeIndex);
                    continueProbe = false;
                }
                else {
                    continueProbe = true;
                }
            }
        }
        DistributedMapRemoveResponse response = new DistributedMapRemoveResponse(removeRequest.getKey(), value);
        this.owner.tell(response, getSelf());
    }
    
    /**
     * Shift back items placed after this now emptySpace
     * These items will be checked to see if they were collisions and aren't in their proper place
     * They will be re-added and put back into their proper place
     * Linearly probes up until an empty entry missing a key value pair
     * Assumes no collisions exist beyond this empty entry as linear probing in addition would have filled it first
     * @param emptySpace
     */
    private void shiftBack(int emptySpace) {
        boolean continueProbe = true;
        for (int i = 1; i < this.bucketSize && continueProbe; i++) {
            int linearProbeIndex = (emptySpace + i) % this.bucketSize;
            Object key = this.keyArray[linearProbeIndex];
            if (key == null) {
                continueProbe = false;
            }
            else {
                int originalIndex = this.originalIndices[linearProbeIndex];
                if (originalIndex != linearProbeIndex) {
                    Object value = this.valueArray[linearProbeIndex];
                    this.keyArray[linearProbeIndex] = null;
                    this.valueArray[linearProbeIndex] = null;
                    this.originalIndices[linearProbeIndex] = -1;
                    this.entryCount--;
                    this.processAdditionRequest(new DistributedMapAdditionRequest(originalIndex, key, value));
                }
                continueProbe = true;
            }
        }
    }
    
    /**
     * Checks for equality between two keys
     * Handles conversion of Object to the appropriate key class
     * @param keyA
     * @param keyB
     * @return
     */
    private boolean equals(Object keyA, Object keyB) {
        boolean equals = (this.kClass.cast(keyA)).equals(this.kClass.cast(keyB));
        return equals;
    }
    
    /**
     * Request a refactor of the whole hash map
     */
    private void refactor() {
        BucketFullRefactorRequest request = new BucketFullRefactorRequest(this.bucketNum);
        this.owner.tell(request, getSelf());
    }
    
    /**
     * Sends back the contents of this bucket to the owner for refactoring
     * @param request
     */
    protected void processRefactorGetRequest(DistributedMapRefactorGetRequest request) {
        for (int i = 0; i < this.bucketSize; i++) {
            Object key = this.keyArray[i];
            if (key != null) {
                Object value = this.valueArray[i];
                this.keyArray[i] = null;
                this.valueArray[i] = null;
                this.originalIndices[i] = -1;
                this.entryCount--;
                DistributedMapRefactorGetResponse response = new DistributedMapRefactorGetResponse(key, value);
                this.owner.tell(response, getSelf());
            }
        }
    }
}
