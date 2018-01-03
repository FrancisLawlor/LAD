package peer.graph.distributedmap;

import java.util.List;
import java.util.Map;

import adt.frame.DistributedMap;
import adt.impl.DistributedMapAdditionResponse;
import adt.impl.DistributedMapContainsResponse;
import adt.impl.DistributedMapGetResponse;
import adt.impl.DistributedMapIterationResponse;
import akka.actor.ActorRef;
import peer.core.PeerToPeerActor;
import peer.core.PeerToPeerActorInit;
import peer.core.UniversalId;
import peer.core.xcept.UnknownMessageException;
import peer.graph.link.PeerLinkExistenceRequest;
import peer.graph.link.PeerLinksRequest;
import peer.graph.weight.LocalWeightUpdateRequest;
import peer.graph.weight.PeerWeightUpdateRequest;
import peer.graph.weight.Weight;
import peer.graph.weight.WeightRequest;

/**
 * Represents theoretical similarity between peers by weighted links
 * Handles Peer graph weighted links in one actor
 * Distributed Hash Map used to map peer ID links to weights
 *
 */
public class PeerWeightedLinkorDHM extends PeerToPeerActor {
    private DistributedMap<UniversalId, Weight> distributedMap;
    
    /**
     * Actor message processing
     */
    @Override
    public void onReceive(Object message) {
        if (message instanceof PeerToPeerActorInit) {
            PeerToPeerActorInit init = (PeerToPeerActorInit) message;
            super.initialisePeerToPeerActor(init);
        }
        else if (message instanceof PeerWeightedLinkAddition) {
            PeerWeightedLinkAddition addition = (PeerWeightedLinkAddition) message;
            this.processPeerWeightedLinkAddition(addition);
        }
        else if (message instanceof PeerLinkExistenceRequest) {
            PeerLinkExistenceRequest request = (PeerLinkExistenceRequest) message;
            this.processPeerLinkExistenceRequest(request);
        }
        else if (message instanceof PeerLinksRequest) {
            PeerLinksRequest peerLinksRequest = (PeerLinksRequest) message;
            this.processPeerLinksRequest(peerLinksRequest);
        }
        else if (message instanceof WeightRequest) {
            WeightRequest weightRequest = (WeightRequest) message;
            this.processWeightRequest(weightRequest);
        }
        else if (message instanceof LocalWeightUpdateRequest) {
            LocalWeightUpdateRequest weightUpdateRequest = (LocalWeightUpdateRequest) message;
            this.processLocalWeightUpdateRequest(weightUpdateRequest);
        }
        else if (message instanceof PeerWeightUpdateRequest) {
            PeerWeightUpdateRequest weightUpdateRequest = (PeerWeightUpdateRequest) message;
            this.processPeerWeightUpdateRequest(weightUpdateRequest);
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
        else if (message instanceof DistributedMapIterationResponse) {
            DistributedMapIterationResponse response = (DistributedMapIterationResponse) message;
            this.processIterationResponse(response);
        }
        else {
            throw new UnknownMessageException();
        }
    }
    
    /**
     * Check if the map contains the link already before adding
     * @param addition
     */
    protected void processPeerWeightedLinkAddition(PeerWeightedLinkAddition addition) {
        
    }
    
    /**
     * 
     * @param request
     */
    protected void processPeerLinkExistenceRequest(PeerLinkExistenceRequest request) {
        UniversalId linkToCheckPeerId = request.getLinkToCheckPeerId();
        this.distributedMap.requestContains(linkToCheckPeerId);
    }
    
    /**
     * 
     * @param request
     */
    protected void processPeerLinksRequest(PeerLinksRequest request) {
        
    }
    
    protected void processWeightRequest(WeightRequest request) {
        
    }
    
    /**
     * 
     * @param request
     */
    protected void processLocalWeightUpdateRequest(LocalWeightUpdateRequest request) {
        
    }
    
    protected void processPeerWeightUpdateRequest(PeerWeightUpdateRequest request) {
        
    }
    
    /**
     * 
     * @param response
     */
    protected void processAdditionResponse(DistributedMapAdditionResponse response) {
        
    }
    
    /**
     * 
     * @param response
     */
    protected void processContainsResponse(DistributedMapContainsResponse response) {
        
    }
    
    /**
     * 
     * @param response
     */
    protected void processGetResponse(DistributedMapGetResponse response) {
        
    }
    
    /**
     * 
     * @param response
     */
    protected void processIterationResponse(DistributedMapIterationResponse response) {
        
    }
}
