package tests.adt;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import adt.frame.DistributedMap;
import adt.impl.DistributedHashMap;
import adt.impl.DistributedMapAdditionResponse;
import adt.impl.DistributedMapContainsResponse;
import adt.impl.DistributedMapGetResponse;
import adt.impl.DistributedMapIterationResponse;
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
    private Map<UniversalId, Weight> iterationTestMap;
    private Map<UniversalId, Weight> getTestMap;
    private Map<UniversalId, Weight> removeTestMap;
    private Map<UniversalId, Weight> containsTest2Map;
    private boolean containsTest2 = false;
    private int testNum = 1;
    private Set<Integer> additionRequestNums;
    private Set<Integer> containsRequestNums;
    private Integer iterationRequestNum;
    private Set<Integer> getRequestNums;
    private Set<Integer> removeRequestNums;
    private Set<Integer> contains2RequestNums;
    
    @Override
    public void onReceive(Object message) {
        try {
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
                this.startIterationTest();
                break;
            case 4:
                this.startGetTest();
                break;
            case 5:
                this.startRemoveTest();
                break;
            case 6:
                this.startContainsTest2();
                break;
            }
            this.testNum++;
        }
        else if (message instanceof DistributedMapAdditionResponse) {
            DistributedMapAdditionResponse response = (DistributedMapAdditionResponse) message;
            if (response.getSuccess()) {
                UniversalId key = this.distributedMap.getAddKey(response);
                if (!this.additionTestMap.containsKey(key)) throw new RuntimeException();
                Weight value = this.additionTestMap.remove(key);
                if (value == null) throw new RuntimeException();
                super.logger.logMessage("Addition Test Progress : " + this.additionTestMap.size() + " ; Key: " + key.toString());
                Integer requestNum = response.getRequestNum();
                if (!this.additionRequestNums.contains(requestNum)) throw new RuntimeException();
                this.additionRequestNums.remove(requestNum);
            }
        }
        else if (message instanceof DistributedMapContainsResponse) {
            DistributedMapContainsResponse response = (DistributedMapContainsResponse) message;
            if (response.getSuccess()) {
                UniversalId key = this.distributedMap.getContainsKey(response);
                if (!this.containsTest2) {
                    boolean contains = response.contains();
                    if (contains) {
                        Weight value = this.containsTestMap.remove(key);
                        super.logger.logMessage("Contains Test Progress: " + this.containsTestMap.size() + " ; Key: " + key.toString() + " ; Contains: " + value.getWeight());
                        Integer requestNum = response.getRequestNum();
                        if (!this.containsRequestNums.contains(requestNum)) throw new RuntimeException();
                        this.containsRequestNums.remove(requestNum);
                        
                    }
                    else {
                        Integer requestNum = response.getRequestNum();
                        this.containsRequestNums.remove(requestNum);
                        requestNum = this.distributedMap.requestContains(key);
                        this.containsRequestNums.add(requestNum);
                    }
                }
                else {
                    boolean contains = response.contains();
                    if (!contains) {
                        Weight value = this.containsTest2Map.remove(key);
                        super.logger.logMessage("Contains Test 2 Progress: " + this.containsTest2Map.size() + " ; Key: " +  key.toString() + " ; Contains: " + value.getWeight());
                        Integer requestNum = response.getRequestNum();
                        if (!this.contains2RequestNums.contains(requestNum)) throw new RuntimeException();
                        this.contains2RequestNums.remove(requestNum);
                    }
                    else {
                        Integer requestNum = response.getRequestNum();
                        this.contains2RequestNums.remove(requestNum);
                        requestNum = this.distributedMap.requestContains(key);
                        this.contains2RequestNums.add(requestNum);
                    }
                    
                }
            }
        }
        else if (message instanceof DistributedMapIterationResponse) {
            DistributedMapIterationResponse response = (DistributedMapIterationResponse) message;
            if (response.getSuccess()) {
                UniversalId key = this.distributedMap.getIterationKey(response);
                Weight value = this.distributedMap.getIterationValue(response);
                Weight valueCheck = this.iterationTestMap.remove(key);
                if (value.getWeight() != valueCheck.getWeight()) throw new RuntimeException(value.getWeight() + " : " + valueCheck.getWeight());
                super.logger.logMessage("Iteration Test Progress: " + this.iterationTestMap.size() + " ; Key: " + key.toString() + " ; Contains: " + value.getWeight());
                Integer requestNum = response.getRequestNum();
                if (!requestNum.equals(this.iterationRequestNum)) throw new RuntimeException(requestNum + " : " + this.iterationRequestNum);
            }
        }
        else if (message instanceof DistributedMapGetResponse) {
            DistributedMapGetResponse response = (DistributedMapGetResponse) message;
            if (response.getSuccess()) {
                UniversalId key = this.distributedMap.getGetKey(response);
                Weight value = (Weight) this.distributedMap.getGetValue(response);
                if (value != null) {
                    Weight valueCheck = this.getTestMap.remove(key);
                    if (value.getWeight() != valueCheck.getWeight()) throw new RuntimeException(value.getWeight() + " : " + valueCheck.getWeight());
                    super.logger.logMessage("Get Test Progress: " + this.getTestMap.size() + " ; Key: " +  key.toString() + " ; Weight: "  + value.getWeight());
                    Integer requestNum = response.getRequestNum();
                    if (!this.getRequestNums.contains(requestNum)) throw new RuntimeException();
                    this.getRequestNums.remove(requestNum);
                }
                else {
                    Integer requestNum = response.getRequestNum();
                    this.getRequestNums.remove(requestNum);
                    requestNum = this.distributedMap.requestGet(key);
                    this.getRequestNums.add(requestNum);
                }
            }
        }
        else if (message instanceof DistributedMapRemoveResponse) {
            DistributedMapRemoveResponse response = (DistributedMapRemoveResponse) message;
            if (response.getSuccess()) {
                UniversalId key = this.distributedMap.getRemoveKey(response);
                Weight value = this.distributedMap.getRemoveValue(response);
                if (value != null) {
                    Weight valueCheck = this.removeTestMap.remove(key);
                    if (value.getWeight() != valueCheck.getWeight()) throw new RuntimeException(value.getWeight() + " : " + valueCheck.getWeight());
                    super.logger.logMessage("Remove Test Progress: " + this.removeTestMap.size() + " ; Key: " + key.toString() + " ; Weight: "  + value.getWeight());
                    Integer requestNum = response.getRequestNum();
                    if (!this.removeRequestNums.contains(requestNum)) throw new RuntimeException();
                    this.removeRequestNums.remove(requestNum);
                }
                else {
                    Integer requestNum = response.getRequestNum();
                    this.removeRequestNums.remove(requestNum);
                    requestNum = this.distributedMap.requestRemove(key);
                    this.removeRequestNums.add(requestNum);
                    
                }
            }
        }
        else if (message instanceof EndTest) {
            if (this.additionTestMap.size() == 0 && this.containsTestMap.size() == 0 && this.iterationTestMap.size() == 0 &&
                    this.getTestMap.size() == 0 && this.removeTestMap.size() == 0 && this.containsTest2Map.size() == 0 &&
                    this.additionRequestNums.size() == 0 && this.containsRequestNums.size() == 0 && this.getRequestNums.size() == 0 &&
                    this.removeRequestNums.size() == 0 && this.contains2RequestNums.size() == 0) {
                super.logger.logMessage("SUCCESS ; All Tests have SUCCEEDED!");
            }
            else {
                super.logger.logMessage("FAIL ; Some Tests have FAILED!");
                super.logger.logMessage("AdditionTestMap Size: " + this.additionTestMap.size());
                super.logger.logMessage("AdditionRequestNums Size: " + this.additionRequestNums.size());
                super.logger.logMessage("ContainsTestMap Size: " + this.containsTestMap.size());
                super.logger.logMessage("ContainsRequestNums Size: " + this.containsRequestNums.size());
                super.logger.logMessage("IterationTestMap Size: " + this.iterationTestMap.size());
                super.logger.logMessage("GetTestMap Size: " + this.getTestMap.size());
                super.logger.logMessage("GetRequestNums Size: " + this.getRequestNums.size());
                super.logger.logMessage("RemoveTestMap Size: " + this.removeTestMap.size());
                super.logger.logMessage("RemoveRequestNums Size: " + this.removeRequestNums.size());
                super.logger.logMessage("ContainsTest2Map Size: " + this.containsTest2Map.size());
                super.logger.logMessage("Contains2RequestNums Size: " + this.contains2RequestNums.size());
            }
        }
        } catch (Exception e) { e.printStackTrace(); }
    }
    
    protected void initialise() {
        this.distributedMap = new DistributedHashMap<UniversalId, Weight>();
        this.distributedMap.initialise(UniversalId.class, Weight.class, getContext(), getSelf(), new UniversalId("Peer2000"));
        this.additionTestMap = new HashMap<UniversalId, Weight>();
        this.containsTestMap = new HashMap<UniversalId, Weight>();
        this.iterationTestMap = new HashMap<UniversalId, Weight>();
        this.getTestMap = new HashMap<UniversalId, Weight>();
        this.removeTestMap = new HashMap<UniversalId, Weight>();
        this.containsTest2Map = new HashMap<UniversalId, Weight>();
        for (int i = 0; i < TestDistributedMap.TEST_SIZE; i++) {
            UniversalId id = new UniversalId("Peer"+ i);
            Weight weight = new Weight(i);
            this.additionTestMap.put(id, weight);
            this.containsTestMap.put(id, weight);
            this.iterationTestMap.put(id, weight);
            this.getTestMap.put(id, weight);
            this.removeTestMap.put(id, weight);
            this.containsTest2Map.put(id, weight);
        }
        this.containsTest2 = false;
        this.additionRequestNums = new HashSet<Integer>();
        this.containsRequestNums = new HashSet<Integer>();
        this.getRequestNums = new HashSet<Integer>();
        this.removeRequestNums = new HashSet<Integer>();
        this.contains2RequestNums = new HashSet<Integer>();
    }
    
    protected void startAdditionTest() {
        Iterator<Entry<UniversalId, Weight>> iterator = additionTestMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry<UniversalId, Weight> entry = iterator.next();
            int requestNum = this.distributedMap.requestAdd(entry.getKey(), entry.getValue());
            this.additionRequestNums.add(requestNum);
            super.logger.logMessage("Addition Test ; Key: " + entry.getKey().toString() + " ; Weight: " + entry.getValue().getWeight());
        }
    }
    
    protected void startContainsTest() {
        Iterator<Entry<UniversalId, Weight>>iterator = containsTestMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry<UniversalId, Weight> entry = iterator.next();
            int requestNum = this.distributedMap.requestContains(entry.getKey());
            this.containsRequestNums.add(requestNum);
            super.logger.logMessage("Contains Test ; Key: " + entry.getKey().toString() + " ; Weight: " + entry.getValue().getWeight());
        }
    }
    
    protected void startIterationTest() {
        int requestNum = this.distributedMap.requestIterator();
        this.iterationRequestNum = requestNum;
        super.logger.logMessage("Requesting iteration");
    }
    
    protected void startGetTest() {
        Iterator<Entry<UniversalId, Weight>> iterator = getTestMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry<UniversalId, Weight> entry = iterator.next();
            int requestNum = this.distributedMap.requestGet(entry.getKey());
            this.getRequestNums.add(requestNum);
            super.logger.logMessage("Get Test ; Key: " + entry.getKey().toString() + " ; Weight: " + entry.getValue().getWeight());
        }
    }
    
    protected void startRemoveTest() {
        Iterator<Entry<UniversalId, Weight>> iterator = removeTestMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry<UniversalId, Weight> entry = iterator.next();
            int requestNum = this.distributedMap.requestRemove(entry.getKey());
            this.removeRequestNums.add(requestNum);
            super.logger.logMessage("Remove Test ; Key: " + entry.getKey().toString() + " ; Weight: " + entry.getValue().getWeight());
        }
    }
    
    protected void startContainsTest2() {
        this.containsTest2 = true;
        Iterator<Entry<UniversalId, Weight>> iterator = containsTest2Map.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry<UniversalId, Weight> entry = iterator.next();
            int requestNum = this.distributedMap.requestContains(entry.getKey());
            this.contains2RequestNums.add(requestNum);
            super.logger.logMessage("Contains Test 2 ; Key: " + entry.getKey().toString() + " ; Weight: " + entry.getValue().getWeight());
        }
    }
}
