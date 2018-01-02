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
    private static final int DEFAULT_BUCKET_SIZE = 127;
    private static final int DEFAULT_BUCKET_COUNT = 7;
    
    private Class<K> kClass;
    private Class<V> vClass;
    private ActorRef owner;
    private ActorRef hashMappor;
    
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
    }
    
    /**
     * Request an addition of a key value pair
     */
    public void requestAdd(K k, V v) {
        DistributedMapAdditionRequest requestAdd = new DistributedMapAdditionRequest(k, v);
        this.hashMappor.tell(requestAdd, this.owner);
    }
    
    /**
     * Ask whether the hash map contains a key
     */
    public void requestContains(K k) {
        DistributedMapContainsRequest requestContains = new DistributedMapContainsRequest(k);
        this.hashMappor.tell(requestContains, this.owner);
    }
    
    /**
     * Try to get the value that has this key
     */
    public void requestGet(K k) {
        DistributedMapGetRequest requestGet = new DistributedMapGetRequest(k);
        this.hashMappor.tell(requestGet, this.owner);
    }
    
    /**
     * Request removal of this key and its value
     */
    public void requestRemove(K k) {
        DistributedMapRemoveRequest requestRemove = new DistributedMapRemoveRequest(k);
        this.hashMappor.tell(requestRemove, this.owner);
    }
    
    /**
     * Request all of the key value pairs in the map
     */
    public void requestIterator() {
        DistributedMapIterationRequest request = new DistributedMapIterationRequest();
        this.hashMappor.tell(request, this.owner);
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
