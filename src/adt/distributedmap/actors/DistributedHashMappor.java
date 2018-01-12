package adt.distributedmap.actors;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import adt.distributedmap.messages.DistributedHashMapBucketInit;
import adt.distributedmap.messages.DistributedHashMapporInit;
import adt.distributedmap.messages.DistributedMapAdditionRequest;
import adt.distributedmap.messages.DistributedMapAdditionResponse;
import adt.distributedmap.messages.DistributedMapBucketAdditionRequest;
import adt.distributedmap.messages.DistributedMapBucketContainsRequest;
import adt.distributedmap.messages.DistributedMapBucketGetRequest;
import adt.distributedmap.messages.DistributedMapBucketRemoveRequest;
import adt.distributedmap.messages.DistributedMapContainsRequest;
import adt.distributedmap.messages.DistributedMapContainsResponse;
import adt.distributedmap.messages.DistributedMapGetRequest;
import adt.distributedmap.messages.DistributedMapGetResponse;
import adt.distributedmap.messages.DistributedMapIterationRequest;
import adt.distributedmap.messages.DistributedMapIterationResponse;
import adt.distributedmap.messages.DistributedMapRefactorAddRequest;
import adt.distributedmap.messages.DistributedMapRefactorAddResponse;
import adt.distributedmap.messages.DistributedMapRefactorGetRequest;
import adt.distributedmap.messages.DistributedMapRefactorGetResponse;
import adt.distributedmap.messages.DistributedMapRemoveRequest;
import adt.distributedmap.messages.DistributedMapRemoveResponse;
import adt.distributedmap.messages.DistributedMapRequest;
import akka.actor.ActorRef;
import akka.actor.PoisonPill;
import akka.actor.Props;
import peer.frame.actors.PeerToPeerActor;
import peer.frame.messages.PeerToPeerActorInit;

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
    private Queue<DistributedMapRequest> newRequestRefactorQueue;
    private Queue<DistributedMapRequest> preRefactorRequestQueue;
    private ArrayList<ActorRef> oldBuckets;
    private int pendingReAdditions;
    private int pendingAdditions;
    private int pendingOtherRequests;
    
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
            this.processAdditionResponse(response);
        }
        else if (message instanceof DistributedMapContainsResponse) {
            DistributedMapContainsResponse response = (DistributedMapContainsResponse) message;
            this.processContainsResponse(response);
        }
        else if (message instanceof DistributedMapGetResponse) {
            DistributedMapGetResponse response = (DistributedMapGetResponse) message;
            this.processGetResponse(response);
        }
        else if (message instanceof DistributedMapRemoveResponse) {
            DistributedMapRemoveResponse response = (DistributedMapRemoveResponse) message;
            this.processRemoveResponse(response);
        }
        else if (message instanceof DistributedMapRefactorGetResponse) {
            DistributedMapRefactorGetResponse response = (DistributedMapRefactorGetResponse) message;
            this.processRefactorGetResponse(response);
        }
        else if (message instanceof DistributedMapRefactorAddResponse) {
            DistributedMapRefactorAddResponse response = (DistributedMapRefactorAddResponse) message;
            this.processRefactorAddition(response);
        }
        else if (message instanceof DistributedMapIterationRequest) {
            DistributedMapIterationRequest request = (DistributedMapIterationRequest) message;
            this.requestIteration(request);
        }
        else if (message instanceof DistributedMapIterationResponse) {
            DistributedMapIterationResponse response = (DistributedMapIterationResponse) message;
            this.owner.tell(response, getSelf());
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
        this.newRequestRefactorQueue = new LinkedList<DistributedMapRequest>();
        this.preRefactorRequestQueue = new LinkedList<DistributedMapRequest>();
        this.pendingReAdditions = 0;
        this.pendingAdditions = 0;
        this.pendingOtherRequests = 0;
    }
    
    /**
     * Creates a new bucketCount sized set of buckets
     */
    private void createBuckets() {
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
     * Bucket name comes from the bucket number in the current Array of Size bucketSize
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
        return Math.abs(scrambledHashCode) % arrayLength;
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
     * @param request
     */
    protected void requestAdd(DistributedMapAdditionRequest request) {
        if (this.refactoring) {
            this.newRequestRefactorQueue.add(request);
        }
        else {
            int requestNum = request.getRequestNum();
            Object k = request.getKey();
            Object v = request.getValue();
            int index = hashFunction(k);
            DistributedMapBucketAdditionRequest addRequest = new DistributedMapBucketAdditionRequest(requestNum, index, k, v);
            ActorRef bucketActor = this.getBucket(index);
            bucketActor.tell(addRequest, getSelf());
            this.pendingAdditions++;
        }
    }
    
    /**
     * Ask whether the hash map contains a key at this index
     * @param request
     */
    protected void requestContains(DistributedMapContainsRequest request) {
        if (this.refactoring) {
            this.newRequestRefactorQueue.add(request);
        }
        else {
            int requestNum = request.getRequestNum();
            Object k = request.getKey();
            int index = hashFunction(k);
            DistributedMapBucketContainsRequest containsRequest = new DistributedMapBucketContainsRequest(requestNum, index, k);
            ActorRef bucketActor = this.getBucket(index);
            bucketActor.tell(containsRequest, getSelf());
            this.pendingOtherRequests++;
        }
    }
    
    /**
     * Try to get the value that has this key in the hash map
     * @param request
     */
    protected void requestGet(DistributedMapGetRequest request) {
        if (this.refactoring) {
            this.newRequestRefactorQueue.add(request);
        }
        else {
            int requestNum = request.getRequestNum();
            Object k = request.getKey();
            int index = hashFunction(k);
            DistributedMapBucketGetRequest getRequest = new DistributedMapBucketGetRequest(requestNum, index, k);
            ActorRef bucketActor = this.getBucket(index);
            bucketActor.tell(getRequest, getSelf());
            this.pendingOtherRequests++;
        }
    }
    
    /**
     * Request removal of this key and its value in the map
     * @param request
     */
    protected void requestRemove(DistributedMapRemoveRequest request) {
        if (this.refactoring) {
            this.newRequestRefactorQueue.add(request);
        }
        else {
            int requestNum = request.getRequestNum();
            Object k = request.getKey();
            int index = hashFunction(k);
            DistributedMapBucketRemoveRequest removeRequest = new DistributedMapBucketRemoveRequest(requestNum, index, k);
            ActorRef bucketActor = this.getBucket(index);
            bucketActor.tell(removeRequest, getSelf());
            this.pendingOtherRequests++;
        }
    }
    
    /**
     * Request Iteration through all key-value pairs in all buckets
     * @param request
     */
    protected void requestIteration(DistributedMapIterationRequest request) {
        if (this.refactoring) {
            this.newRequestRefactorQueue.add(request);
        }
        else {
            for (ActorRef bucketor : this.buckets) {
                bucketor.tell(request, getSelf());
            }
        }
    }
    
    /**
     * Tries the Addition request again in the next bucket if it failed
     * Leaves in Queue for later if refactoring
     */
    protected void processAdditionResponse(DistributedMapAdditionResponse response) {
        boolean success = response.getSuccess();
        if (success) {
            this.entryCount++;
            this.pendingAdditions--;
            this.owner.tell(response, getSelf());
            if (this.refactoring) {
                this.allAdditionsCommittedCheck();
            }
        }
        else {
            int requestNum = response.getRequestNum();
            Object k = response.getKey();
            Object v = response.getValue();
            if (this.refactoring) {
                DistributedMapAdditionRequest request = new DistributedMapAdditionRequest(requestNum, k, v);
                this.preRefactorRequestQueue.add(request);
                this.pendingAdditions--;
                this.allAdditionsCommittedCheck();
            }
            else {
                int nextBucketToTry = (response.getBucketNum() + 1) % this.bucketCount;
                ActorRef nextBucket = this.buckets.get(nextBucketToTry);
                DistributedMapBucketAdditionRequest request = new DistributedMapBucketAdditionRequest(requestNum, 0, k, v);
                nextBucket.tell(request, getSelf());
            }
        }
    }
    
    /**
     * Tries the Contains request again in the next bucket if it failed
     * Leaves in Queue for later if refactoring
     */
    protected void processContainsResponse(DistributedMapContainsResponse response) {
        boolean success = response.getSuccess();
        if (success) {
            this.pendingOtherRequests--;
            this.owner.tell(response, getSelf());
            if (this.refactoring) {
                this.refactorOverCheck();
            }
            else {
                this.refactorCheck();
            }
        }
        else {
            int requestNum = response.getRequestNum();
            Object k = response.getKey();
            if (this.refactoring) {
                DistributedMapContainsRequest request = new DistributedMapContainsRequest(requestNum, k);
                this.preRefactorRequestQueue.add(request);
                this.pendingOtherRequests--;
                this.refactorOverCheck();
            }
            else {
                int nextBucketToTry = (response.getBucketNum() + 1) % this.bucketCount;
                ActorRef nextBucket = this.buckets.get(nextBucketToTry);
                DistributedMapBucketContainsRequest request = new DistributedMapBucketContainsRequest(requestNum, 0, k);
                nextBucket.tell(request, getSelf());
            }
        }
    }
    
    /**
     * Tries the Get request again in the next bucket if it failed
     * Leaves in Queue for later if refactoring
     */
    protected void processGetResponse(DistributedMapGetResponse response) {
        boolean success = response.getSuccess();
        if (success) {
            this.pendingOtherRequests--;
            this.owner.tell(response, getSelf());
            if (this.refactoring) {
                this.refactorOverCheck();
            }
        }
        else {
            int requestNum = response.getRequestNum();
            Object k = response.getKey();
            if (this.refactoring) {
                DistributedMapGetRequest request = new DistributedMapGetRequest(requestNum, k);
                this.preRefactorRequestQueue.add(request);
                this.pendingOtherRequests--;
                this.refactorOverCheck();
            }
            else {
                int nextBucketToTry = (response.getBucketNum() + 1) % this.bucketCount;
                ActorRef nextBucket = this.buckets.get(nextBucketToTry);
                DistributedMapBucketGetRequest request = new DistributedMapBucketGetRequest(requestNum, 0, k);
                nextBucket.tell(request, getSelf());
            }
        }
    }
    
    /**
     * Tries the Remove request again in the next bucket if it failed
     * Leaves in Queue for later if refactoring
     */
    protected void processRemoveResponse(DistributedMapRemoveResponse response) {
        boolean success = response.getSuccess();
        if (success) {
            this.entryCount--;
            this.pendingOtherRequests--;
            this.owner.tell(response, getSelf());
            if (this.refactoring) {
                this.refactorOverCheck();
            }
        }
        else {
            int requestNum = response.getRequestNum();
            Object k = response.getKey();
            if (this.refactoring) {
                DistributedMapRemoveRequest request = new DistributedMapRemoveRequest(requestNum, k);
                this.preRefactorRequestQueue.add(request);
                this.pendingOtherRequests--;
                this.refactorOverCheck();
            }
            else {
                int nextBucketToTry = (response.getBucketNum() + 1) % this.bucketCount;
                ActorRef nextBucket = this.buckets.get(nextBucketToTry);
                DistributedMapBucketRemoveRequest request = new DistributedMapBucketRemoveRequest(requestNum, 0, k);
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
    
    /**
     * Refactor the Distributed HashMap if its total size exceeds a threshold
     */
    protected void refactor() {
        this.refactoring = true;
    }
    
    /**
     * Checks if all additions have been committed before requesting the buckets are refactored
     */
    private void allAdditionsCommittedCheck() {
        if (this.pendingAdditions < 1) {
            this.refactorBuckets();
        }
    }
    
    /**
     * Calls for all buckets to send back their contents for refactoring into new buckets
     */
    private void refactorBuckets() {
        this.oldBuckets = this.buckets;
        this.bucketCount = ((this.bucketCount + 1) * 2) - 1;
        this.createBuckets();
        for (int i = 0; i< this.oldBuckets.size(); i++) {
            ActorRef oldBucket = this.oldBuckets.get(i);
            oldBucket.tell(new DistributedMapRefactorGetRequest(i), getSelf());
        }
    }
    
    /**
     * When an old key value pair is received from an old bucket call for its re-addition into the new buckets
     */
    protected void processRefactorGetResponse(DistributedMapRefactorGetResponse response) {
        Object k = response.getKey();
        Object v = response.getValue();
        this.requestReAdd(k, v);
        this.entryCount--;
    }
    
    /**
     * Requests re-addition of the old key value pairs into the new buckets
     * @param k
     * @param v
     */
    private void requestReAdd(Object k, Object v) {
        int index = hashFunction(k);
        DistributedMapRefactorAddRequest readdRequest = new DistributedMapRefactorAddRequest(index, k, v);
        int bucketNum = this.getBucketNum(index);
        ActorRef bucketActor = this.buckets.get(bucketNum);
        bucketActor.tell(readdRequest, getSelf());
        this.pendingReAdditions++;
    }
    
    /**
     * Tries another bucket if the refactor addition failed in this bucket
     * Otherwise considers the re-addition a success and checks if all re-additions are done and refactoring is over
     * @param response
     */
    protected void processRefactorAddition(DistributedMapRefactorAddResponse response) {
        boolean success = response.getSuccess();
        if (success) {
            this.entryCount++;
            this.pendingReAdditions--;
            if (this.refactoring) {
                this.refactorOverCheck();
            }
        }
        else {
            Object k = response.getKey();
            Object v = response.getValue();
            int nextBucketToTry = (response.getBucketNum() + 1) % this.bucketCount;
            ActorRef nextBucket = this.buckets.get(nextBucketToTry);
            DistributedMapRefactorAddRequest request = new DistributedMapRefactorAddRequest(0, k, v);
            nextBucket.tell(request, getSelf());
        }
    }
    
    /**
     * Checks if the refactoring period should end
     */
    private void refactorOverCheck() {
        if (this.pendingReAdditions + this.pendingAdditions + this.pendingOtherRequests < 1) {
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
            oldBucket.tell(PoisonPill.getInstance(), getSelf());
        }
        this.oldBuckets.clear();
        this.oldBuckets = null;
        this.refactoring = false;
        while (!this.preRefactorRequestQueue.isEmpty()) {
            DistributedMapRequest request = this.preRefactorRequestQueue.remove();
            this.reRequest(request);
        }
        while (!this.newRequestRefactorQueue.isEmpty()) {
            DistributedMapRequest request = this.newRequestRefactorQueue.remove();
            this.reRequest(request);
        }
    }
    
    /**
     * Attempts to request again after request being denied and buffered in a queue during refactoring
     * @param request
     */
    private void reRequest(DistributedMapRequest request) {
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
        case DistributedMapIterationRequest:
            DistributedMapIterationRequest iterationRequest = (DistributedMapIterationRequest) request;
            this.requestIteration(iterationRequest);
        default:
            break;
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
}
