package peer.core;

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
    PeerLinkExistenceRequest("PeerLinkExistenceRequest"),
    PeerLinkExistenceResponse("PeerLinkExistenceResponse"),
    LocalWeightUpdateRequest("LocalWeightUpdateRequest"),
    PeerWeightUpdateRequest("PeerWeightUpdateRequest"),
    WeighterInit("WeighterInit"),
    WeightRequest("WeightRequest"),
    WeightResponse("WeightResponse"),
    PeerSimilarViewAlert("PeerSimilarViewAlert"),
    ContentFileExistenceRequest("ContentFileExistenceRequest"),
    ContentFileExistenceResponse("ContentFileExistenceResponse"),
    ContentFileRequest("ContentFileRequest"),
    ContentFileResponse("ContentFileResponse"),
    SimilarContentViewPeerRequest("SimilarContentViewPeerRequest"),
    SimilarContentViewPeerResponse("SimilarContentViewPeerResponse"),
    RetrievedContentFile("RetrievedContentFile"),
    ContentViewAddition("ContentViewAddition"),
    RecordContentView("RecordContentView"),
    DistributedHashMapporInit("DistributedHashMapporInit"),
    DistributedHashMapBucketInit("DistributedHashMapBucketInit"),
    DistributedMapAdditionResponse("DistributedMapAdditionResponse"),
    DistributedMapContainsResponse("DistributedMapContainsResponse"),
    DistributedMapGetResponse("DistributedMapGetResponse"),
    DistributedMapRemoveResponse("DistributedMapRemoveResponse"),
    DistributedMapRefactorGetRequest("DistributedMapRefactorGetRequest"),
    DistributedMapRefactorGetResponse("DistributedMapRefactorGetResponse"),
    DistributedMapBucketAdditionRequest("DistributedMapBucketAdditionRequest"),
    DistributedMapBucketContainsRequest("DistributedMapBucketContainsRequest"),
    DistributedMapBucketGetRequest("DistributedMapBucketGetRequest"),
    DistributedMapBucketRemoveRequest("DistributedMapBucketRemoveRequest"),
    DistributedMapAdditionRequest("DistributedMapAdditionRequest"),
    DistributedMapContainsRequest("DistributedMapContainsRequest"),
    DistributedMapGetRequest("DistributedMapGetRequest"),
    DistributedMapRemoveRequest("DistributedMapRemoveRequest");
    
    private String typeName;
    
    private ActorMessageType(String typeName) {
        this.typeName = typeName;
    }
    
    public String getTypeName() {
        return this.typeName;
    }
}
