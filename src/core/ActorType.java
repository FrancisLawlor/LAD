package core;

/**
 * Enumerates Actor Types
 *
 */
public enum ActorType {
    Viewer("Viewer"),
    ViewHistorian("ViewHistorian"),
    PeerLinker("PeerLinker"),
    InboundCommunicator("InboundCommunicator"),
    OutboundCommunicator("OutboundCommunicator"),
    Recommender("Recommender"),
    HistoryRecommendationGenerator("HistoryRecommendationGenerator"),
    PeerAggregator("PeerAggregator"),
    Retriever("Retriever"),
    Weighter("Weighter");
    
    private String type;
    
    private ActorType(String type) {
        this.type = type;
    }
    
    public String getTypeName() {
        return this.type;
    }
}
