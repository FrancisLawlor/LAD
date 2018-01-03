package adt.impl;

import adt.frame.DistributedMap;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActorContext;
import peer.core.PeerToPeerActorInit;
import peer.core.UniversalId;

/**
 * Distributed Hash Map adapter for DistributedHashMappor actor
 *
 * @param <K>
 * @param <V>
 */
public class DistributedHashMap<K, V> implements DistributedMap<K, V> {
    private static final String NAME = "DistributedHashMappor";
    private static final int DEFAULT_BUCKET_SIZE = 255;
    private static final int DEFAULT_BUCKET_COUNT = 7;
    
    private Class<K> kClass;
    private Class<V> vClass;
    private ActorRef owner;
    private ActorRef hashMappor;
    private int requestNumber;
    
    /**
     * Initialise the Distributed Hash Map
     * @param kClass
     * @param vClass
     * @param actorContext
     */
    public void initialise(Class<K> kClass, Class<V> vClass, UntypedActorContext actorContext, ActorRef owner, UniversalId peerId) {
        this.kClass = kClass;
        this.vClass = vClass;
        this.owner = owner;
        this.hashMappor = actorContext.actorOf(Props.create(DistributedHashMappor.class), NAME);
        PeerToPeerActorInit peerIdInit = new PeerToPeerActorInit(peerId, NAME);
        this.hashMappor.tell(peerIdInit, owner);
        DistributedHashMapporInit init = new DistributedHashMapporInit(DEFAULT_BUCKET_SIZE, DEFAULT_BUCKET_COUNT, kClass);
        this.hashMappor.tell(init, owner);
        this.requestNumber = 0;
    }
    
    /**
     * Request an addition of a key value pair
     */
    public int requestAdd(K k, V v) {
        int requestNum = this.requestNumber++;
        DistributedMapAdditionRequest requestAdd = new DistributedMapAdditionRequest(requestNum, k, v);
        this.hashMappor.tell(requestAdd, this.owner);
        return requestNum;
    }
    
    /**
     * Ask whether the hash map contains a key
     */
    public int requestContains(K k) {
        int requestNum = this.requestNumber++;
        DistributedMapContainsRequest requestContains = new DistributedMapContainsRequest(requestNum, k);
        this.hashMappor.tell(requestContains, this.owner);
        return requestNum;
    }
    
    /**
     * Try to get the value that has this key
     */
    public int requestGet(K k) {
        int requestNum = this.requestNumber++;
        DistributedMapGetRequest requestGet = new DistributedMapGetRequest(requestNum, k);
        this.hashMappor.tell(requestGet, this.owner);
        return requestNum;
    }
    
    /**
     * Request removal of this key and its value
     */
    public int requestRemove(K k) {
        int requestNum = this.requestNumber++;
        DistributedMapRemoveRequest requestRemove = new DistributedMapRemoveRequest(requestNum, k);
        this.hashMappor.tell(requestRemove, this.owner);
        return requestNum;
    }
    
    /**
     * Request all of the key value pairs in the map
     */
    public int requestIterator() {
        int requestNum = this.requestNumber++;
        DistributedMapIterationRequest request = new DistributedMapIterationRequest(requestNum);
        this.hashMappor.tell(request, this.owner);
        return requestNum;
    }
    
    public K getAddKey(DistributedMapAdditionResponse response) {
        return this.kClass.cast(response.getKey());
    }
    
    public V getAddValue(DistributedMapAdditionResponse response) {
        return this.vClass.cast(response.getValue());
    }
    
    public K getContainsKey(DistributedMapContainsResponse response) {
        return this.kClass.cast(response.getKey());
    }
    
    public boolean getContains(DistributedMapContainsResponse response) {
        return response.contains();
    }
    
    public K getGetKey(DistributedMapGetResponse response) {
        return this.kClass.cast(response.getKey());
    }
    
    public V getGetValue(DistributedMapGetResponse response) {
        return this.vClass.cast(response.getValue());
    }
    
    public K getRemoveKey(DistributedMapRemoveResponse response) {
        return this.kClass.cast(response.getKey());
    }
    
    public V getRemoveValue(DistributedMapRemoveResponse response) {
        return this.vClass.cast(response.getValue());
    }
    
    public K getIterationKey(DistributedMapIterationResponse response) {
        return this.kClass.cast(response.getKey());
    }
    
    public V getIterationValue(DistributedMapIterationResponse response) {
        return this.vClass.cast(response.getValue());
    }
}
