package peer.frame.messages;

import peer.frame.core.ActorMessageType;
import peer.frame.core.UniversalId;

public class PeerToPeerRequest extends ActorMessage {
    private UniversalId origin;
    private UniversalId target;
    
    public PeerToPeerRequest(ActorMessageType type, UniversalId origin, UniversalId target) {
        super(type);
        this.origin = origin;
        this.target = target;
    }
    
    public UniversalId getOriginalRequester() {
        return this.origin;
    }
    
    public UniversalId getOriginalTarget() {
        return this.target;
    }
}
