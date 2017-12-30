package adt.impl;

import java.util.ArrayList;

import adt.frame.DistributedMap;
import akka.actor.ActorRef;
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
    private static final int INIT_BUCKET_NUM = 8;
    private static final String NAME_PREFIX = "DistributedHashMapBucket";
    
    private Class<K> kClass;
    private Class<V> vClass;
    private UniversalId peerId;
    private ActorRef owner;
    private ArrayList<ActorRef> buckets;
    private int arrayLength;
    private int entryCount;
    
    public DistributedHashMap(UntypedActorContext context, ActorRef owner, UniversalId peerId, Class<K> kClass, Class<V> vClass) {
        this.entryCount = 0;
        this.arrayLength = INIT_BUCKET_NUM * BUCKET_SIZE;
        this.kClass = kClass;
        this.vClass = vClass;
        this.peerId = peerId;
        this.owner = owner;
        this.buckets = new ArrayList<ActorRef>();
        for (int i = 0; i < INIT_BUCKET_NUM; i++) {
            ActorRef bucket = context.actorOf(Props.create(DistributedHashMapBucket.class), getBucketName(i));
            PeerToPeerActorInit peerIdInit = new PeerToPeerActorInit(this.peerId, getBucketName(i));
            bucket.tell(peerIdInit, owner);
            DistributedHashMapBucketInit init = new DistributedHashMapBucketInit(this.owner, i, BUCKET_SIZE, kClass); 
            bucket.tell(init, owner);
            this.buckets.add(bucket);
        }
    }
    
    /**
     * Bucket name comes from the bucket number in the current Array of Size this.arrayLength
     * @param i
     * @return
     */
    private final String getBucketName(int i) {
        return NAME_PREFIX + "_" + i + "_inArraySize_" + this.arrayLength;
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
        bucketActor.tell(addRequest, owner);
    }
    
    /**
     * Ask whether the hash map contains a key at this index
     */
    public void requestContains(K k) {
        int index = hashFunction(k);
        ActorRef bucketActor = find(index);
        DistributedMapContainsRequest containsRequest = new DistributedMapContainsRequest(index, k);
        bucketActor.tell(containsRequest, owner);
    }
    
    /**
     * Try to get the value that has this key in the hash map
     */
    public void requestGet(K k) {
        int index = hashFunction(k);
        ActorRef bucketActor = find(index);
        DistributedMapGetRequest getRequest = new DistributedMapGetRequest(index, k);
        bucketActor.tell(getRequest, owner);
    }
    
    /**
     * Request removal of this key and its value in the map
     */
    public void requestRemove(K k) {
        int index = hashFunction(k);
        ActorRef bucketActor = find(index);
        DistributedMapRemoveRequest removeRequest = new DistributedMapRemoveRequest(index, k);
        bucketActor.tell(removeRequest, owner);
    }
    
    /**
     * Refactor if a bucket gets too full
     */
    public void refactor() {
        
    }
    
    public int size() {
        return this.entryCount;
    }
    
    public int capacity() {
        return this.arrayLength;
    }
    
    public K getAddKey(DistributedMapAdditionResponse response) {
        return kClass.cast(response.getKey());
    }
    
    public V getAddValue(DistributedMapAdditionResponse response) {
        return vClass.cast(response.getValue());
    }
    
    public K getContainsKey(DistributedMapContainsResponse response) {
        return kClass.cast(response.getKey());
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
