package core;

public class RequestCommunication extends ActorMessage {
    private UniversalId origin;
    private UniversalId target;
    
    public RequestCommunication(UniversalId origin, UniversalId target) {
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
