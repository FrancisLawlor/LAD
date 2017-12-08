package core;

public class ActorMessage {
    private RequestHistory requestHistory;
    
    public ActorMessage() {
        this.requestHistory = new RequestHistory();
    }
    
    public RequestHistory getRequestHistory() {
        return this.requestHistory;
    }
}
