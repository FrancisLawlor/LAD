package adt.impl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import adt.frame.DistributedMap;
import akka.actor.ActorRef;
import akka.actor.PoisonPill;
import akka.actor.Props;
import akka.actor.UntypedActorContext;
import peer.core.PeerToPeerActorInit;
import peer.core.UniversalId;

/**
 * Distributed Hash Map that splits the array into Distributed Hash Map Bucket Actors
 *
 * @param <K>
 * @param <V>
 */
public class DistributedHashMap<K, V> implements DistributedMap<K, V> {
    private static final int BUCKET_SIZE = 128;
    private static final int INIT_BUCKET_COUNT = 8;
    private static final String NAME_PREFIX = "DistributedHashMapBucket";
    
    private Class<K> kClass;
    private Class<V> vClass;
    private UniversalId peerId;
    private UntypedActorContext context;
    private ActorRef owner;
    private ArrayList<ActorRef> buckets;
    private int bucketCount;
    private int entryCount;
    private boolean refactoring;
    private Queue<DistributedMapRequest> refactorQueue;
    private int refactorResponsesExpected;
    private ArrayList<ActorRef> oldBuckets;
    
    public DistributedHashMap(UntypedActorContext context, ActorRef owner, UniversalId peerId, Class<K> kClass, Class<V> vClass) {
        this.kClass = kClass;
        this.vClass = vClass;
        this.peerId = peerId;
        this.owner = owner;
        this.context = context;
        this.entryCount = 0;
        this.bucketCount = INIT_BUCKET_COUNT;
        this.createBuckets();
        this.refactoring = false;
        this.refactorQueue = new LinkedList<DistributedMapRequest>();
        this.refactorResponsesExpected = 0;
    }
    
    private void createBuckets() {
        this.buckets = new ArrayList<ActorRef>();
        for (int i = 0; i < this.bucketCount; i++) {
            ActorRef bucket = this.context.actorOf(Props.create(DistributedHashMapBucket.class), getBucketName(i));
            PeerToPeerActorInit peerIdInit = new PeerToPeerActorInit(this.peerId, getBucketName(i));
            bucket.tell(peerIdInit, this.owner);
            DistributedHashMapBucketInit init = new DistributedHashMapBucketInit(this.owner, i, BUCKET_SIZE, this.kClass); 
            bucket.tell(init, this.owner);
            this.buckets.add(bucket);
        }
    }
    
    /**
     * Bucket name comes from the bucket number in the current Array of Size this.arrayLength
     * @param i
     * @return
     */
    private final String getBucketName(int i) {
        return NAME_PREFIX + "_" + i + "/" + this.bucketCount;
    }
    
    /**
     * Hashes the Key and gets the index in the theoretical array
     * @param k
     * @return
     */
    private int hashFunction(K k) {
        int arrayLength = BUCKET_SIZE * this.buckets.size();
        return Math.abs(k.hashCode() % arrayLength);
    }
    
    /**
     * Get the bucket number;
     * @param index
     * @return
     */
    private int getBucketNum(int index) {
        return index / BUCKET_SIZE;
    }
    
    /**
     * Find the index in the array
     * @param index
     * @return
     */
    private ActorRef find(int index) {
        int bucketNum = getBucketNum(index);
        ActorRef bucketActor = this.buckets.get(bucketNum);
        return bucketActor;
    }
    
    /**
     * Request an addition of a key value pair at the hash index
     */
    public void requestAdd(K k, V v) {
        int index = hashFunction(k);
        ActorRef bucketActor = find(index);
        DistributedMapAdditionRequest addRequest = new DistributedMapAdditionRequest(index, k, v);
        if (this.refactoring) {
            this.refactorQueue.add(addRequest);
        }
        else {
            bucketActor.tell(addRequest, this.owner);
            this.entryCount++;
        }
    }
    
    /**
     * Ask whether the hash map contains a key at this index
     */
    public void requestContains(K k) {
        int index = hashFunction(k);
        ActorRef bucketActor = find(index);
        DistributedMapContainsRequest containsRequest = new DistributedMapContainsRequest(index, k);
        if (this.refactoring) {
            this.refactorQueue.add(containsRequest);
        }
        else {
            bucketActor.tell(containsRequest, this.owner);
        }
    }
    
    /**
     * Try to get the value that has this key in the hash map
     */
    public void requestGet(K k) {
        int index = hashFunction(k);
        ActorRef bucketActor = find(index);
        DistributedMapGetRequest getRequest = new DistributedMapGetRequest(index, k);
        if (this.refactoring) {
            this.refactorQueue.add(getRequest);
        }
        else {
            bucketActor.tell(getRequest, this.owner);
        }
    }
    
    /**
     * Request removal of this key and its value in the map
     */
    public void requestRemove(K k) {
        int index = hashFunction(k);
        ActorRef bucketActor = find(index);
        DistributedMapRemoveRequest removeRequest = new DistributedMapRemoveRequest(index, k);
        if (this.refactoring) {
            this.refactorQueue.add(removeRequest);
        }
        else {
            bucketActor.tell(removeRequest, this.owner);
            this.entryCount--;
        }
    }
    
    /**
     * Refactor if a bucket gets too full
     */
    public void requestRefactor() {
        this.refactoring = true;
        this.oldBuckets = this.buckets;
        this.bucketCount *= 2;
        this.createBuckets();
        this.refactorResponsesExpected = this.entryCount;
        this.entryCount = 0;
        for (int i = 0; i< this.oldBuckets.size(); i++) {
            ActorRef oldBucket = this.oldBuckets.get(i);
            oldBucket.tell(new DistributedMapRefactorGetRequest(i), this.owner);
        }
    }
    
    /**
     * Add old entries from the old buckets into the new buckets
     */
    public void setRefactorGetResponse(DistributedMapRefactorGetResponse response) {
        K key = this.kClass.cast(response.getKey());
        V value = this.vClass.cast(response.getValue());
        this.requestAdd(key, value);
        this.refactorResponsesExpected--;
        if (this.refactorResponsesExpected == 0) {
            this.endRefactoring();
        }
    }
    
    /**
     * End the refactoring of the Distributed Hash Map
     * Wipes the old buckets clean
     * Processes all the Queued Requests in the queue, checking their type first
     */
    private void endRefactoring() {
        for (ActorRef oldBucket : this.oldBuckets) {
            oldBucket.tell(PoisonPill.getInstance(), this.owner);
        }
        this.oldBuckets.clear();
        this.oldBuckets = null;
        while (!this.refactorQueue.isEmpty()) {
            DistributedMapRequest request = this.refactorQueue.remove();
            K key = this.kClass.cast(request.getKey());
            switch (request.getType()) {
                case DistributedMapAdditionRequest:
                    DistributedMapAdditionRequest additionRequest = (DistributedMapAdditionRequest) request;
                    V value = this.vClass.cast(additionRequest.getValue());
                    this.requestAdd(key, value);
                    break;
                case DistributedMapContainsRequest:
                    this.requestContains(key);
                    break;
                case DistributedMapGetRequest:
                    this.requestGet(key);
                    break;
                case DistributedMapRemoveRequest:
                    this.requestRemove(key);
                    break;
                default:
                    break;
            }
        }
        this.refactoring = false;
    }
    
    /**
     * Returns the number of entries in the Distributed Hash Map
     */
    public int size() {
        return this.entryCount;
    }
    
    /**
     * Returns potential capacity of the Distributed Hash Map
     */
    public int capacity() {
        return this.bucketCount * BUCKET_SIZE;
    }
    
    public K getAddKey(DistributedMapAdditionResponse response) {
        return kClass.cast(response.getKey());
    }
    
    public V getAddValue(DistributedMapAdditionResponse response) {
        return vClass.cast(response.getValue());
    }
    
    public boolean getAdditionSuccessful(DistributedMapAdditionResponse response) {
        return response.isAdditionSuccessful();
    }
    
    public K getContainsKey(DistributedMapContainsResponse response) {
        return kClass.cast(response.getKey());
    }
    
    public boolean getContains(DistributedMapContainsResponse response) {
        return response.contains();
    }
    
    public K getGetKey(DistributedMapGetResponse response) {
        return kClass.cast(response.getKey());
    }
    
    public V getGetValue(DistributedMapGetResponse response) {
        return vClass.cast(response.getValue());
    }
    
    public K getRemoveKey(DistributedMapRemoveResponse response) {
        return kClass.cast(response.getKey());
    }
    
    public V getRemoveValue(DistributedMapRemoveResponse response) {
        return vClass.cast(response.getValue());
    }
}
