package tests.adt;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import adt.frame.DistributedMap;
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
    private Map<UniversalId, Weight> containsTest2Map;
    private boolean containsTest2 = false;
    private int testNum = 1;
    private int addFails;
    private int containsFails;
    private int getFails;
    private int removeFails;
    
    @Override
    public void onReceive(Object message) {
        if (message instanceof PeerToPeerActorInit) {
            PeerToPeerActorInit init = (PeerToPeerActorInit) message;
            super.initialisePeerToPeerActor(init);
        }
        else if (message instanceof DummyInit) {
            super.logger = ((DummyInit)message).getLogger();
            this.initialise();
        }
        else if (message instanceof StartTest) {
            switch (this.testNum) {
            case 1:
                this.startAdditionTest();
                break;
            case 2:
                this.startContainsTest();
                break;
            case 3:
                this.startGetTest();
                break;
            case 4:
                this.startRemoveTest();
                break;
            case 5:
                this.startContainsTest2();
                break;
            }
            this.testNum++;
        }
        else if (message instanceof DistributedMapAdditionResponse) {
            DistributedMapAdditionResponse response = (DistributedMapAdditionResponse) message;
            if (response.getSuccess()) {
                UniversalId key = (UniversalId) response.getKey();
                if (!this.additionTestMap.containsKey(key)) throw new RuntimeException();
                Weight value = this.additionTestMap.remove(key);
                if (value == null) throw new RuntimeException();
                super.logger.logMessage("Addition Test Progress : " + this.additionTestMap.size() + " ; Key: " + key.toString());
            }
            else this.addFails++;
        }
        else if (message instanceof DistributedMapContainsResponse) {
            DistributedMapContainsResponse response = (DistributedMapContainsResponse) message;
            if (response.getSuccess()) {
                UniversalId key = (UniversalId) response.getKey();
                if (!this.containsTest2) {
                    boolean contains = response.contains();
                    if (contains) {
                        Weight value = this.containsTestMap.remove(key);
                        super.logger.logMessage("Contains Test Progress: " + this.containsTestMap.size() + " ; Key: " + key.toString() + " ; Contains: " + value.getWeight());
                    }
                }
                else {
                    boolean contains = response.contains();
                    if (!contains) {
                        Weight value = this.containsTest2Map.remove(key);
                        super.logger.logMessage("Contains Test Progress: " + this.containsTest2Map.size() + " ; Key: " +  key.toString() + " ; Contains: " + value.getWeight());
                    }
                    
                }
            }
            else this.containsFails++;
        }
        else if (message instanceof DistributedMapGetResponse) {
            DistributedMapGetResponse response = (DistributedMapGetResponse) message;
            if (response.getSuccess()) {
                UniversalId key = (UniversalId) response.getKey();
                Weight value = (Weight) response.getValue();
                if (value != null) {
                    Weight valueCheck = this.getTestMap.remove(key);
                    if (value.getWeight() != valueCheck.getWeight()) throw new RuntimeException();
                    super.logger.logMessage("Get Test Progress: " + this.getTestMap.size() + " ; Key: " +  key.toString() + " ; Weight: "  + value.getWeight());
                }
            }
            else this.getFails++;
        }
        else if (message instanceof DistributedMapRemoveResponse) {
            DistributedMapRemoveResponse response = (DistributedMapRemoveResponse) message;
            if (response.getSuccess()) {
                UniversalId key = (UniversalId) response.getKey();
                Weight value = (Weight) response.getValue();
                if (value != null) {
                    Weight valueCheck = this.removeTestMap.remove(key);
                    if (value.getWeight() != valueCheck.getWeight()) throw new RuntimeException();
                    super.logger.logMessage("Remove Test Progress: " + this.removeTestMap.size() + " ; Key: " + key.toString() + " ; Weight: "  + value.getWeight());
                }
            }
            else this.removeFails++;
        }
        else if (message instanceof EndTest) {
            if (this.additionTestMap.size() == 0 && this.containsTestMap.size() == 0 && 
                    this.getTestMap.size() == 0 && this.removeTestMap.size() == 0 && this.containsTest2Map.size() == 0) {
                super.logger.logMessage("SUCCESS ; All Tests have SUCCEEDED!");
            }
            else {
                super.logger.logMessage("FAIL ; Some Tests have FAILED!");
                super.logger.logMessage("AdditionTestMap Size: " + this.additionTestMap.size());
                super.logger.logMessage("AdditionTest Fails: " + this.addFails);
                super.logger.logMessage("ContainsTestMap Size: " + this.containsTestMap.size());
                super.logger.logMessage("ContainsTest Fails: " + this.containsFails);
                super.logger.logMessage("GetTestMap Size: " + this.getTestMap.size());
                super.logger.logMessage("GetTest Fails: " + this.getFails);
                super.logger.logMessage("RemoveTestMap Size: " + this.removeTestMap.size());
                super.logger.logMessage("RemoveTest Fails: " + this.removeFails);
                super.logger.logMessage("ContainsTest2Map Size: " + this.containsTest2Map.size());
            }
        }
    }
    
    protected void initialise() {
        this.distributedMap = new DistributedHashMap<UniversalId, Weight>();
        this.distributedMap.initialise(UniversalId.class, Weight.class, getContext(), getSelf(), new UniversalId("Peer2000"));
        this.additionTestMap = new HashMap<UniversalId, Weight>();
        this.containsTestMap = new HashMap<UniversalId, Weight>();
        this.getTestMap = new HashMap<UniversalId, Weight>();
        this.removeTestMap = new HashMap<UniversalId, Weight>();
        this.containsTest2Map = new HashMap<UniversalId, Weight>();
        for (int i = 0; i < 5000; i++) {
            UniversalId id = new UniversalId("Peer"+ i);
            Weight weight = new Weight(i);
            this.additionTestMap.put(id, weight);
            this.containsTestMap.put(id, weight);
            this.getTestMap.put(id, weight);
            this.removeTestMap.put(id, weight);
            this.containsTest2Map.put(id, weight);
        }
        this.containsTest2 = false;
        this.addFails = 0;
        this.containsFails = 0;
        this.getFails = 0;
        this.removeFails = 0;
    }
    
    protected void startAdditionTest() {
        Iterator<Entry<UniversalId, Weight>> iterator = additionTestMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry<UniversalId, Weight> entry = iterator.next();
            this.distributedMap.requestAdd(entry.getKey(), entry.getValue());
            super.logger.logMessage("Addition Test ; Key: " + entry.getKey().toString() + " ; Weight: " + entry.getValue().getWeight());
        }
    }
    
    protected void startContainsTest() {
        Iterator<Entry<UniversalId, Weight>>iterator = containsTestMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry<UniversalId, Weight> entry = iterator.next();
            this.distributedMap.requestContains(entry.getKey());
            super.logger.logMessage("Contains Test ; Key: " + entry.getKey().toString() + " ; Weight: " + entry.getValue().getWeight());
        }
    }
    
    protected void startGetTest() {
        Iterator<Entry<UniversalId, Weight>> iterator = getTestMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry<UniversalId, Weight> entry = iterator.next();
            this.distributedMap.requestGet(entry.getKey());
            super.logger.logMessage("Get Test ; Key: " + entry.getKey().toString() + " ; Weight: " + entry.getValue().getWeight());
        }
    }
    
    protected void startRemoveTest() {
        Iterator<Entry<UniversalId, Weight>> iterator = removeTestMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry<UniversalId, Weight> entry = iterator.next();
            this.distributedMap.requestRemove(entry.getKey());
            super.logger.logMessage("Remove Test ; Key: " + entry.getKey().toString() + " ; Weight: " + entry.getValue().getWeight());
        }
    }
    
    protected void startContainsTest2() {
        this.containsTest2 = true;
        Iterator<Entry<UniversalId, Weight>> iterator = containsTest2Map.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry<UniversalId, Weight> entry = iterator.next();
            this.distributedMap.requestContains(entry.getKey());
            super.logger.logMessage("Contains Test 2 ; Key: " + entry.getKey().toString() + " ; Weight: " + entry.getValue().getWeight());
        }
    }
}
