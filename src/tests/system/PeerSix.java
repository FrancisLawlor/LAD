package tests.system;

import peer.frame.core.UniversalId;
import peer.graph.core.PeerWeightedLink;
import peer.graph.core.Weight;

public class PeerSix extends SystemTestPeerToPeerActorSystem implements Runnable {
    
    public void run() {
        PeerSix peer = new PeerSix();
        try {
            peer.createActors(new UniversalId(SystemTestConstants.PEER_SIX));
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
        
        addPeer(new PeerWeightedLink(new UniversalId(SystemTestConstants.PEER_SEVEN), new Weight(70)));
        addPeer(new PeerWeightedLink(new UniversalId(SystemTestConstants.PEER_EIGHT), new Weight(80)));
        addPeer(new PeerWeightedLink(new UniversalId(SystemTestConstants.PEER_NINE), new Weight(90)));
        addPeer(new PeerWeightedLink(new UniversalId(SystemTestConstants.PEER_TEN), new Weight(100)));
    }
}
