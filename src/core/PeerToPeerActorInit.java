package core;

public class PeerToPeerActorInit extends ActorMessage {
    private UniversalId peerId;
    private String nameOfActorInSystem;
    
    public PeerToPeerActorInit(UniversalId peerId, String nameOfActorInSystem) {
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
