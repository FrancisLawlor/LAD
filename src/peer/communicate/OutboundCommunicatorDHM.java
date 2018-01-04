package peer.communicate;

import peer.core.UniversalId;
import peer.graph.distributedmap.RemotePeerWeightedLinkAddition;

/**
 * OutboundCommunicator variant that communicates with PeerWeightedLinkorDHM
 *
 */
public class OutboundCommunicatorDHM extends OutboundCommunicator {
    /**
     * Actor Message processing
     */
    @Override
    public void onReceive(Object message) {
        if (message instanceof RemotePeerWeightedLinkAddition) {
            RemotePeerWeightedLinkAddition addition = (RemotePeerWeightedLinkAddition) message;
            this.processRemotePeerWeightedLinkAddition(addition);
        }
        else {
            super.onReceive(message);
        }
    }
    
    /**
     * Sends an outbound request to add a weighted link to the peer graph of a remote peer
     * This is required if a weighted link has been added to the local peer's peer graph
     * Seeks to keep weighted links consistent on both ends
     * @param addition
     */
    protected void processRemotePeerWeightedLinkAddition(RemotePeerWeightedLinkAddition addition) {
        UniversalId peerId = addition.getOriginalTarget();
        String restletUri = CamelRestletUris.getRemotePeerWeightedLinkAddition(peerId);
        String additionJson = this.gson.toJson(addition);
        this.camelTemplate.sendBody(restletUri, additionJson);
    }
}
