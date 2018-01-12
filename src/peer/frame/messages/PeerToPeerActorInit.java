package peer.frame.messages;

import peer.frame.core.ActorMessageType;
import peer.frame.core.UniversalId;

/**
 * Initialises a PeerToPeerActor with its Peer ID and Name in Peer Actor System
 *
 */
public class PeerToPeerActorInit extends ActorMessage {
    private UniversalId peerId;
    private String nameOfActorInSystem;
    
    public PeerToPeerActorInit(UniversalId peerId, String nameOfActorInSystem) {
        super(ActorMessageType.PeerToPeerActorInit);
        this.peerId = peerId;
        this.nameOfActorInSystem = nameOfActorInSystem;
    }
    
    public UniversalId getPeerId() {
        return this.peerId;
    }
    
    public String getActorName() {
        return this.nameOfActorInSystem;
    }
}
