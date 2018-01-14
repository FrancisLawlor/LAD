package tests.system;

import peer.frame.core.UniversalId;
import peer.graph.core.PeerWeightedLink;
import peer.graph.core.Weight;

public class PeerSeven extends SystemTestPeerToPeerActorSystem implements Runnable {
    
    public void run() {
        PeerSeven peer = new PeerSeven();
        try {
            peer.createActors(new UniversalId(SystemTestConstants.PEER_SEVEN));
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
        
        addPeer(new PeerWeightedLink(new UniversalId(SystemTestConstants.PEER_EIGHT), new Weight(80)));
        addPeer(new PeerWeightedLink(new UniversalId(SystemTestConstants.PEER_NINE), new Weight(90)));
        addPeer(new PeerWeightedLink(new UniversalId(SystemTestConstants.PEER_TEN), new Weight(100)));
    }
}
