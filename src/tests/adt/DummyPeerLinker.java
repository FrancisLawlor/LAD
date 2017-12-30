package tests.adt;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import adt.frame.DistributedMap;
import adt.impl.BucketFullRefactorRequest;
import adt.impl.DistributedHashMap;
import adt.impl.DistributedMapAdditionResponse;
import adt.impl.DistributedMapContainsResponse;
import adt.impl.DistributedMapGetResponse;
import adt.impl.DistributedMapRemoveResponse;
import peer.core.PeerToPeerActorInit;
import peer.core.UniversalId;
import peer.graph.weight.Weight;
import tests.core.DummyActor;
import tests.core.DummyInit;
import tests.core.StartTest;

public class DummyPeerLinker extends DummyActor {
    private DistributedMap<UniversalId, Weight> distributedMap;
    
    private Map<UniversalId, Weight> additionTestMap;
    private Map<UniversalId, Weight> containsTestMap;
    private Map<UniversalId, Weight> getTestMap;
    private Map<UniversalId, Weight> removeTestMap;
    
    public DummyPeerLinker() {
        this.initialise();
    }
    
    @Override
    public void onReceive(Object message) {
        if (message instanceof PeerToPeerActorInit) {
            PeerToPeerActorInit init = (PeerToPeerActorInit) message;
            super.initialisePeerToPeerActor(init);
        }
        else if (message instanceof DummyInit) {
            super.logger = ((DummyInit)message).getLogger();
        }
        else if (message instanceof StartTest) {
            this.startTest();
        }
        else if (message instanceof DistributedMapAdditionResponse) {
            DistributedMapAdditionResponse response = (DistributedMapAdditionResponse) message;
            UniversalId key = this.distributedMap.getAddKey(response);
            boolean success = response.isAdditionSuccessful();
            Weight value = this.distributedMap.getAddValue(response);
            this.additionTestMap.remove(key);
            if (value != null || !success) throw new RuntimeException();
        }
        else if (message instanceof DistributedMapContainsResponse) {
            DistributedMapContainsResponse response = (DistributedMapContainsResponse) message;
            UniversalId key = this.distributedMap.getContainsKey(response);
            boolean value = response.contains();
            if (!value) throw new RuntimeException();
            Weight valueCheck = this.containsTestMap.remove(key);
            if (valueCheck == null) throw new RuntimeException();
        }
        else if (message instanceof DistributedMapGetResponse) {
            DistributedMapGetResponse response = (DistributedMapGetResponse) message;
            UniversalId key = this.distributedMap.getGetKey(response);
            Weight value = this.distributedMap.getGetValue(response);
            Weight valueCheck = this.getTestMap.remove(key);
            if (value.getWeight() != valueCheck.getWeight()) throw new RuntimeException();
        }
        else if (message instanceof DistributedMapRemoveResponse) {
            DistributedMapRemoveResponse response = (DistributedMapRemoveResponse) message;
            UniversalId key = this.distributedMap.getRemoveKey(response);
            Weight value = this.distributedMap.getRemoveValue(response);
            Weight valueCheck = this.removeTestMap.remove(key);
            if (value.getWeight() != valueCheck.getWeight()) throw new RuntimeException();
        }
        else if (message instanceof EndTest) {
            if (this.additionTestMap.size() == 0 && this.containsTestMap.size() == 0 && this.getTestMap.size() == 0 && this.removeTestMap.size() == 0) {
                super.logger.logMessage("SUCCESS");
            }
            else {
                super.logger.logMessage("FAIL");
                super.logger.logMessage("AdditionTestMap Size: " + this.additionTestMap.size());
                super.logger.logMessage("ContainsTestMap Size: " + this.containsTestMap.size());
                super.logger.logMessage("GetTestMap Size: " + this.getTestMap.size());
                super.logger.logMessage("RemoveTestMap Size: " + this.removeTestMap.size());
            }
        }
        else if (message instanceof BucketFullRefactorRequest){
            throw new RuntimeException("bucketfull");
        }
    }
    
    protected void initialise() {
        this.additionTestMap = new HashMap<UniversalId, Weight>();
        this.containsTestMap = new HashMap<UniversalId, Weight>();
        this.getTestMap = new HashMap<UniversalId, Weight>();
        this.removeTestMap = new HashMap<UniversalId, Weight>();
        for (int i = 0; i < 400; i++) {
            UniversalId id = new UniversalId("Peer"+ i);
            Weight weight = new Weight(i);
            this.additionTestMap.put(id, weight);
            this.containsTestMap.put(id, weight);
            this.getTestMap.put(id, weight);
            this.removeTestMap.put(id, weight);
        }
        this.distributedMap = new DistributedHashMap<UniversalId, Weight>(getContext(), getSelf(), super.peerId, UniversalId.class, Weight.class);
    }
    
    protected void startTest() {
        Iterator<Entry<UniversalId, Weight>> iterator = additionTestMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry<UniversalId, Weight> entry = iterator.next();
            this.distributedMap.requestAdd(entry.getKey(), entry.getValue());
        }
        iterator = containsTestMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry<UniversalId, Weight> entry = iterator.next();
            this.distributedMap.requestContains(entry.getKey());
        }
        iterator = getTestMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry<UniversalId, Weight> entry = iterator.next();
            this.distributedMap.requestGet(entry.getKey());
        }
        iterator = removeTestMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry<UniversalId, Weight> entry = iterator.next();
            this.distributedMap.requestRemove(entry.getKey());
        }
    }
}
