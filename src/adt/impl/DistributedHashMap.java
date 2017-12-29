package adt.impl;

import java.util.ArrayList;

import adt.frame.DistributedMap;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import peer.core.PeerToPeerActorInit;
import peer.core.UniversalId;

public class DistributedHashMap<K, V> implements DistributedMap<K, V> {
    private static final int BUCKET_SIZE = 100;
    private static final int INIT_BUCKET_NUM = 5;
    private static final String NAME_PREFIX = "DistributedHashMapBucket";
    
    private Class<K> kClass;
    private Class<V> vClass;
    private UniversalId peerId;
    private ActorRef owner;
    private ArrayList<ActorRef> buckets;
    private int arrayLength;
    
    public DistributedHashMap(ActorSystem actorSystem, ActorRef owner, UniversalId peerId, Class<K> kClass, Class<V> vClass) {
        this.arrayLength = INIT_BUCKET_NUM * BUCKET_SIZE;
        this.kClass = kClass;
        this.vClass = vClass;
        this.peerId = peerId;
        this.owner = owner;
        this.buckets = new ArrayList<ActorRef>();
        for (int i = 0; i < INIT_BUCKET_NUM; i++) {
            ActorRef bucket = actorSystem.actorOf(Props.create(DistributedHashMapBucket.class), getBucketName(i));
            PeerToPeerActorInit peerIdInit = new PeerToPeerActorInit(this.peerId, getBucketName(i));
            bucket.tell(peerIdInit, owner);
            DistributedHashMapBucketInit init = new DistributedHashMapBucketInit(i, BUCKET_SIZE, kClass, vClass); 
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
        return k.hashCode() % arrayLength;
    }
    
    private int findBucket(int index) {
        return index / BUCKET_SIZE;
    }
    
    private ActorRef find(int index) {
        int bucketNum = findBucket(index);
        ActorRef bucketActor = this.buckets.get(bucketNum);
        return bucketActor;
    }
    
    public void requestAdd(K k, V v) {
        int index = hashFunction(k);
        ActorRef bucketActor = find(index);
        DistributedMapAdditionRequest addRequest = new DistributedMapAdditionRequest(index, k, v);
        bucketActor.tell(addRequest, owner);
    }
    
    public void requestContains(K k) {
        int index = hashFunction(k);
        ActorRef bucketActor = find(index);
        DistributedMapContainsRequest containsRequest = new DistributedMapContainsRequest(index, k);
        bucketActor.tell(containsRequest, owner);
    }
    
    public void requestGet(K k) {
        int index = hashFunction(k);
        ActorRef bucketActor = find(index);
        DistributedMapGetRequest getRequest = new DistributedMapGetRequest(index, k);
        bucketActor.tell(getRequest, owner);
    }
    
    public void requestRemove(K k) {
        int index = hashFunction(k);
        ActorRef bucketActor = find(index);
        DistributedMapRemoveRequest removeRequest = new DistributedMapRemoveRequest(index, k);
        bucketActor.tell(removeRequest, owner);
    }
}
