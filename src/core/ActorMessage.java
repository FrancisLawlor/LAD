package core;

public class ActorMessage {
    private MessageTrace messageTrace;
    
    public ActorMessage() {
        this.messageTrace = new MessageTrace();
    }
    
    public ActorMessage(MessageTrace trace) {
        this.messageTrace = trace;
    }
    
    public MessageTrace getMessageTrace() {
        return this.messageTrace;
    }
    
    public UniversalId getOriginalRequester() {
        return this.messageTrace.getOriginalRequester();
    }
}
