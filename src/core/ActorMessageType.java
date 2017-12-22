package core;

/**
 * Enumerates Actor Message Types
 *
 */
public enum ActorMessageType {
    PeerRecommendationForUserRequest("PeerRecommendationForUserRequest"),
    PeerRecommendationsForUser("PeerRecommendationsForUser"),
    PeerRecommendationRequest("PeerRecommendationRequest"),
    PeerRecommendation("PeerRecommendation"),
    HistoryRecommendationGeneratorInit("HistoryRecommendationGeneratorInit"),
    PeerRecommendationAggregatorInit("PeerRecommendationAggregatorInit"),
    LocalRetrieveContentRequest("LocalRetrieveContentRequest"),
    PeerRetrieveContentRequest("PeerRetrieveContentRequest"),
    RetrievedContent("RetrievedContent"),
    ViewerInit("ViewerInit"),
    ViewHistoryRequest("ViewHistoryRequest"),
    ViewHistoryResponse("ViewHistoryResponse"),
    PeerToPeerActorInit("PeerToPeerActorInit"),
    OutboundCommInit("OutboundCommInit"),
    AddressChangeAnnounce("AddressChangeAnnounce"),
    AddressChangedAcknowledged("AddressChangedAcknowledged"),
    GossipInit("GossipInit"),
    NewAddressForPeer("NewAddressForPeer"),
    OldPeerAddressesRequest("OldPeerAddressesRequest"),
    OldPeerAddressResponse("OldPeerAddressResponse"),
    ResolvePeerAddressRequest("ResolvePeerAddressRequest"),
    PeerLinkAddition("PeerLinkAddition"),
    PeerLinkResponse("PeerLinkResponse"),
    PeerLinksRequest("PeerLinksRequest"),
    LocalWeightUpdateRequest("LocalWeightUpdateRequest"),
    PeerWeightUpdateRequest("PeerWeightUpdateRequest"),
    WeighterInit("WeighterInit"),
    WeightRequest("WeightRequest"),
    WeightResponse("WeightResponse");
    
    
    private String typeName;
    
    private ActorMessageType(String typeName) {
        this.typeName = typeName;
    }
    
    public String getTypeName() {
        return this.typeName;
    }
}
