package core;

public class RequestCommunication extends ActorMessage {
    private UniversalId origin;
    private UniversalId target;
    
    public RequestCommunication(ActorMessageType type, UniversalId origin, UniversalId target) {
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
