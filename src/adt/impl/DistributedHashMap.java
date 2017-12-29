package adt.impl;

import java.util.ArrayList;

import adt.frame.DistributedMap;
import akka.actor.ActorRef;

public class DistributedHashMap<K, V> implements DistributedMap<K, V> {
    private ArrayList<ActorRef> buckets;
    private ActorRef owner;
    
    public DistributedHashMap(ActorRef owner) {
        this.owner = owner;
    }
    
    public void add(K k, V v) {
        
    }
    
    public boolean contains(K k) {
        return false;
    }
    
    public V remove(K k) {
        return null;
    }
}
