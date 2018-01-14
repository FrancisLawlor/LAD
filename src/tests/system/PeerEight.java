package tests.system;

import peer.frame.core.UniversalId;
import peer.graph.core.PeerWeightedLink;
import peer.graph.core.Weight;

public class PeerEight extends SystemTestPeerToPeerActorSystem implements Runnable {
    
    public void run() {
        PeerEight peer = new PeerEight();
        try {
            peer.createActors(new UniversalId(SystemTestConstants.PEER_EIGHT));
            while (true) {
                Thread.sleep(10);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void createTestingSystem() {
        try {
            Thread.sleep(SystemTestConstants.TIME_TO_WAIT);
        } catch (Exception e) { };
        
        addPeer(new PeerWeightedLink(new UniversalId(SystemTestConstants.PEER_NINE), new Weight(90)));
        addPeer(new PeerWeightedLink(new UniversalId(SystemTestConstants.PEER_TEN), new Weight(100)));
    }
}
