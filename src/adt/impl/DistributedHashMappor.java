package adt.impl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import akka.actor.ActorRef;
import akka.actor.PoisonPill;
import akka.actor.Props;
import peer.core.PeerToPeerActor;
import peer.core.PeerToPeerActorInit;

/**
 * Distributed Hash Map that splits the array into Distributed Hash Map Bucket Actors
 *
 * @param <K>
 * @param <V>
 */
public class DistributedHashMappor extends PeerToPeerActor {
    private static final double THRESHOLD = 0.9;
    private static final String NAME_PREFIX = "DistributedHashMapBucket";
    
    private Class<?> kClass;
    private ActorRef owner;
    private ArrayList<ActorRef> buckets;
    private int bucketSize;
    private int bucketCount;
    private int entryCount;
    private boolean refactoring;
    private Queue<DistributedMapRequest> refactorQueue;
    private int refactorResponsesExpected;
    private ArrayList<ActorRef> oldBuckets;
    
    /**
     * Actor message processing
     */
    @Override
    public void onReceive(Object message) {
        if (message instanceof PeerToPeerActorInit) {
            PeerToPeerActorInit init = (PeerToPeerActorInit) message;
            this.initialisePeerToPeerActor(init);
        }
        else if (message instanceof DistributedHashMapporInit) {
            DistributedHashMapporInit init = (DistributedHashMapporInit) message;
            this.initialise(init);
        }
        else if (message instanceof DistributedMapAdditionRequest) {
            DistributedMapAdditionRequest request = (DistributedMapAdditionRequest) message;
            this.requestAdd(request);
        }
        else if (message instanceof DistributedMapContainsRequest) {
            DistributedMapContainsRequest request = (DistributedMapContainsRequest) message;
            this.requestContains(request);
        }
        else if (message instanceof DistributedMapGetRequest) {
            DistributedMapGetRequest request = (DistributedMapGetRequest) message;
            this.requestGet(request);
        }
        else if (message instanceof DistributedMapRemoveRequest) {
            DistributedMapRemoveRequest request = (DistributedMapRemoveRequest) message;
            this.requestRemove(request);
        }
        else if (message instanceof DistributedMapAdditionResponse) {
            DistributedMapAdditionResponse response = (DistributedMapAdditionResponse) message;
            this.processAddition(response);
        }
        else if (message instanceof DistributedMapContainsResponse) {
            DistributedMapContainsResponse response = (DistributedMapContainsResponse) message;
            this.processContains(response);
        }
        else if (message instanceof DistributedMapGetResponse) {
            DistributedMapGetResponse response = (DistributedMapGetResponse) message;
            this.processGet(response);
        }
        else if (message instanceof DistributedMapRemoveResponse) {
            DistributedMapRemoveResponse response = (DistributedMapRemoveResponse) message;
            this.processRemove(response);
        }
        else if (message instanceof DistributedMapRefactorGetResponse) {
            DistributedMapRefactorGetResponse response = (DistributedMapRefactorGetResponse) message;
            this.processRefactorGetResponse(response);
        }
    }
    
    /**
     * Initialise the DistributedHashMappor
     * @param init
     */
    protected void initialise(DistributedHashMapporInit init) {
        this.kClass = init.getKClass();
        this.owner = getSender();
        this.bucketSize = init.getBucketSize();
        this.bucketCount = init.getBucketCount();
        this.createBuckets();
        this.refactoring = false;
        this.refactorQueue = new LinkedList<DistributedMapRequest>();
        this.refactorResponsesExpected = 0;
    }
    
    /**
     * Creates a new bucketCount sized set of buckets
     */
    private void createBuckets() {
        this.entryCount = 0;
        this.buckets = new ArrayList<ActorRef>();
        for (int i = 0; i < this.bucketCount; i++) {
            ActorRef bucket = getContext().actorOf(Props.create(DistributedHashMapBucketor.class), getBucketName(i));
            PeerToPeerActorInit peerIdInit = new PeerToPeerActorInit(this.peerId, getBucketName(i));
            bucket.tell(peerIdInit, getSelf());
            DistributedHashMapBucketInit init = new DistributedHashMapBucketInit(getSelf(), i, this.bucketSize, this.kClass); 
            bucket.tell(init, getSelf());
            this.buckets.add(bucket);
        }
    }
    
    /**
     * Bucket name comes from the bucket number in the current Array of Size this.arrayLength
     * @param i
     * @return
     */
    private final String getBucketName(int i) {
        return NAME_PREFIX + "_" + i + "_OutOf_" + this.bucketCount;
    }
    
    /**
     * Hashes the Key and gets the index in the theoretical array
     * @param k
     * @return
     */
    private int hashFunction(Object k) {
        int arrayLength = this.capacity();
        int hashCode = k.hashCode();
        int scrambledHashCode = mix(hashCode);
        return Math.abs(scrambledHashCode % arrayLength);
    }
    
    /**
     * Golden ratio
     */
    private static final int INT_PHI = 0x9E3779B9;
    
    /**
     * Bit mixer
     * @author Koloboke 
     * @contact https://github.com/OpenHFT/Koloboke
     * @param x
     * @return
     */
    private static final int mix(final int x) {
        final int h = x * INT_PHI;
        return h ^ (h >>> 16);
    }
    
    /**
     * Get the bucket number;
     * @param index
     * @return
     */
    private int getBucketNum(int index) {
        return index / this.bucketSize;
    }
    
    /**
     * Get the bucketor
     * @param bucketNum
     * @return
     */
    private ActorRef getBucket(int index) {
        int bucketNum = this.getBucketNum(index);
        return this.buckets.get(bucketNum);
    }
    
    /**
     * Request an addition of a key value pair at the hash index
     */
    protected void requestAdd(DistributedMapAdditionRequest request) {
        if (this.refactoring) {
            this.refactorQueue.add(request);
        }
        else {
            Object k = request.getKey();
            Object v = request.getValue();
            int index = hashFunction(k);
            DistributedMapBucketAdditionRequest addRequest = new DistributedMapBucketAdditionRequest(index, k, v);
            ActorRef bucketActor = this.getBucket(index);
            bucketActor.tell(addRequest, getSelf());
        }
    }
    
    /**
     * Ask whether the hash map contains a key at this index
     */
    protected void requestContains(DistributedMapContainsRequest request) {
        if (this.refactoring) {
            this.refactorQueue.add(request);
        }
        else {
            Object k = request.getKey();
            int index = hashFunction(k);
            DistributedMapBucketContainsRequest containsRequest = new DistributedMapBucketContainsRequest(index, k);
            ActorRef bucketActor = this.getBucket(index);
            bucketActor.tell(containsRequest, getSelf());
        }
    }
    
    /**
     * Try to get the value that has this key in the hash map
     */
    protected void requestGet(DistributedMapGetRequest request) {
        if (this.refactoring) {
            this.refactorQueue.add(request);
        }
        else {
            Object k = request.getKey();
            int index = hashFunction(k);
            DistributedMapBucketGetRequest getRequest = new DistributedMapBucketGetRequest(index, k);
            ActorRef bucketActor = this.getBucket(index);
            bucketActor.tell(getRequest, getSelf());
        }
    }
    
    /**
     * Request removal of this key and its value in the map
     */
    protected void requestRemove(DistributedMapRemoveRequest request) {
        if (this.refactoring) {
            this.refactorQueue.add(request);
        }
        else {
            Object k = request.getKey();
            int index = hashFunction(k);
            DistributedMapBucketRemoveRequest removeRequest = new DistributedMapBucketRemoveRequest(index, k);
            ActorRef bucketActor = this.getBucket(index);
            bucketActor.tell(removeRequest, getSelf());
        }
    }
    
    /**
     * Refactor the Distributed HashMap if its total size exceeds a threshold
     */
    protected void refactor() {
        this.refactoring = true;
        this.refactorResponsesExpected = this.size();
        this.oldBuckets = this.buckets;
        this.bucketCount = (this.bucketCount * 2) - 1;
        this.createBuckets();
        for (int i = 0; i< this.oldBuckets.size(); i++) {
            ActorRef oldBucket = this.oldBuckets.get(i);
            oldBucket.tell(new DistributedMapRefactorGetRequest(i), getSelf());
        }
    }
    
    /**
     * Add old entries from the old buckets into the new buckets
     */
    protected void processRefactorGetResponse(DistributedMapRefactorGetResponse response) {
        Object k = response.getKey();
        Object v = response.getValue();
        this.requestReAdd(k, v);
        this.refactorResponsesExpected--;
        if (this.refactorResponsesExpected == 0) {
            this.endRefactoring();
        }
    }
    
    /**
     * Readds entries into buckets during Refactoring of the Distributed Hash Map
     * @param k
     * @param v
     */
    private void requestReAdd(Object k, Object v) {
        int index = hashFunction(k);
        DistributedMapBucketAdditionRequest readdRequest = new DistributedMapBucketAdditionRequest(index, k, v);
        int bucketNum = this.getBucketNum(index);
        ActorRef bucketActor = this.buckets.get(bucketNum);
        bucketActor.tell(readdRequest, getSelf());
    }
    
    /**
     * End the refactoring of the Distributed Hash Map
     * Wipes the old buckets clean
     * Processes all the Queued Requests in the queue, checking their type first
     */
    private void endRefactoring() {
        for (ActorRef oldBucket : this.oldBuckets) {
            oldBucket.tell(PoisonPill.getInstance(), getSelf());
        }
        this.oldBuckets.clear();
        this.oldBuckets = null;
        this.refactoring = false;
        while (!this.refactorQueue.isEmpty()) {
            DistributedMapRequest request = this.refactorQueue.remove();
            switch (request.getType()) {
                case DistributedMapAdditionRequest:
                    DistributedMapAdditionRequest additionRequest = (DistributedMapAdditionRequest) request;
                    this.requestAdd(additionRequest);
                    break;
                case DistributedMapContainsRequest:
                    DistributedMapContainsRequest containsRequest = (DistributedMapContainsRequest) request;
                    this.requestContains(containsRequest);
                    break;
                case DistributedMapGetRequest:
                    DistributedMapGetRequest getRequest = (DistributedMapGetRequest) request;
                    this.requestGet(getRequest);
                    break;
                case DistributedMapRemoveRequest:
                    DistributedMapRemoveRequest removeRequest = (DistributedMapRemoveRequest) request;
                    this.requestRemove(removeRequest);
                    break;
                default:
                    break;
            }
        }
    }
    
    /**
     * Returns the number of entries in the Distributed Hash Map
     */
    protected int size() {
        return this.entryCount;
    }
    
    /**
     * Returns potential capacity of the Distributed Hash Map
     */
    protected int capacity() {
        return this.bucketCount * this.bucketSize;
    }
    
    /**
     * Tries the Addition request again in the next bucket if it failed
     */
    protected void processAddition(DistributedMapAdditionResponse response) {
        boolean success = response.getSuccess();
        if (success) {
            this.entryCount++;
            this.owner.tell(response, getSelf());
            this.refactorCheck();
        }
        else {
            Object k = response.getKey();
            Object v = response.getValue();
            if (this.refactoring) {
                DistributedMapAdditionRequest request = new DistributedMapAdditionRequest(k, v);
                this.requestAdd(request);
            }
            else {
                int nextBucketToTry = (response.getBucketNum() + 1) % this.bucketCount;
                ActorRef nextBucket = this.buckets.get(nextBucketToTry);
                DistributedMapBucketAdditionRequest request = new DistributedMapBucketAdditionRequest(0, k, v);
                nextBucket.tell(request, getSelf());
            }
        }
    }
    
    /**
     * Tries the Contains request again in the next bucket if it failed
     */
    protected void processContains(DistributedMapContainsResponse response) {
        boolean success = response.getSuccess();
        if (success) {
            this.owner.tell(response, getSelf());
        }
        else {
            Object k = response.getKey();
            if (this.refactoring) {
                DistributedMapContainsRequest request = new DistributedMapContainsRequest(k);
                this.requestContains(request);
            }
            else {
                int nextBucketToTry = (response.getBucketNum() + 1) % this.bucketCount;
                ActorRef nextBucket = this.buckets.get(nextBucketToTry);
                DistributedMapBucketContainsRequest request = new DistributedMapBucketContainsRequest(0, k);
                nextBucket.tell(request, getSelf());
            }
        }
    }
    
    /**
     * Tries the Get request again in the next bucket if it failed
     */
    protected void processGet(DistributedMapGetResponse response) {
        boolean success = response.getSuccess();
        if (success) {
            this.owner.tell(response, getSelf());
        }
        else {
            Object k = response.getKey();
            if (this.refactoring) {
                DistributedMapGetRequest request = new DistributedMapGetRequest(k);
                this.requestGet(request);
            }
            else {
                int nextBucketToTry = (response.getBucketNum() + 1) % this.bucketCount;
                ActorRef nextBucket = this.buckets.get(nextBucketToTry);
                DistributedMapBucketGetRequest request = new DistributedMapBucketGetRequest(0, k);
                nextBucket.tell(request, getSelf());
            }
        }
    }
    
    /**
     * Tries the Remove request again in the next bucket if it failed
     */
    protected void processRemove(DistributedMapRemoveResponse response) {
        boolean success = response.getSuccess();
        if (success) {
            this.entryCount--;
            this.owner.tell(response, getSelf());
        }
        else {
            Object k = response.getKey();
            if (this.refactoring) {
                DistributedMapRemoveRequest request = new DistributedMapRemoveRequest(k);
                this.requestRemove(request);
            }
            else {
                int nextBucketToTry = (response.getBucketNum() + 1) % this.bucketCount;
                ActorRef nextBucket = this.buckets.get(nextBucketToTry);
                DistributedMapBucketRemoveRequest request = new DistributedMapBucketRemoveRequest(0, k);
                nextBucket.tell(request, getSelf());
            }
        }
    }
    
    /**
     * Check if a refactor is needed
     */
    private void refactorCheck() {
        if (this.size() > THRESHOLD * this.capacity()) {
            this.refactor();
        }
    }
}
