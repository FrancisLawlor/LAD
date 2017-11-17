package graph.weight;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;

/**
 * Represents a theoretical weight of a link 
 * between the local user and a remote peer
 * Handles Weight Requests
 * Handles Updates to keep link weight consistent for both peers
 *
 */
public class Weighter extends UntypedActor {
    
    private double linkWeight = 0;
    
    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof WeightRequest) {
            WeightRequest weightRequest = 
                    (WeightRequest) message;
            this.processWeightRequest(weightRequest);
        }
        else if (message instanceof LocalWeightUpdateRequest) {
            LocalWeightUpdateRequest weightUpdateRequest = 
                    (LocalWeightUpdateRequest) message;
            this.processLocalWeightUpdateRequest(weightUpdateRequest);
        }
        else if (message instanceof PeerWeightUpdateRequest) {
            PeerWeightUpdateRequest weightUpdateRequest = 
                    (PeerWeightUpdateRequest) message;
            this.processPeerWeightUpdateRequest(weightUpdateRequest);
        }
        else {
            throw new RuntimeException("Unrecognised Message; Debug");
        }
    }
    
    /**
     * Sends back the weight of the theoretical edge/link between users
     * @param weightRequest
     */
    protected void processWeightRequest(WeightRequest weightRequest) {
        WeightResponse weightResponse = new WeightResponse(this.linkWeight);
        ActorRef requester = getSender();
        requester.tell(weightResponse, getSelf());
    }
    
    /**
     * Updates weight from local request
     * @param updateRequest
     */
    protected void processLocalWeightUpdateRequest(LocalWeightUpdateRequest updateRequest) {
        this.linkWeight = updateRequest.getNewWeight();
        // To do, send PeerWeightUpdateRequest to peer
    }
    
    /**
     * Updates weight due to peer request
     * Maintains consistent link weight in both frames of reference
     * @param updateRequest
     */
    protected void processPeerWeightUpdateRequest(PeerWeightUpdateRequest updateRequest) {
        this.linkWeight = updateRequest.getNewWeight();
        //To do, add security to check the request is from the correct user?
    }
}
