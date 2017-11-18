package peer.communicate;

import akka.actor.UntypedActor;

/**
 * Handles communication between peers
 * Converts Actor Messages into REST
 *
 */
public class Communicator extends UntypedActor {
    
    @Override
    public void onReceive(Object message) throws Throwable {
        
    }
    
    // To do:
    // Apache Camel for HTTP
    // REST conversion of Actor Messages that need to be sent peer to peer (eg. PeerRecommendationRequest)
    //
}
