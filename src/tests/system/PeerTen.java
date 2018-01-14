package tests.system;

import peer.frame.core.UniversalId;

public class PeerTen extends SystemTestPeerToPeerActorSystem implements Runnable {
        
        public void run() {
            PeerTen peer = new PeerTen();
            try {
                peer.createActors(new UniversalId(SystemTestConstants.PEER_TEN));
                while (true) {
                    Thread.sleep(10);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
}
