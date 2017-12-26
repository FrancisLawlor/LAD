package peer.core;

public class ActorNames {
    public static final String VIEWER = "viewer";
    public static final String VIEW_HISTORIAN = "viewHistorian";
    public static final String PEER_LINKER = "peerLinker";
    public static final String INBOUND_COMM = "inboundCommunicator";
    public static final String OUTBOUND_COMM = "outboundCommunicator";
    public static final String RECOMMENDER = "recommender";
    public static final String HISTORY_GENERATOR = "historyGenerator";
    public static final String AGGREGATOR = "peerAggregator";
    public static final String RETRIEVER = "retriever";
    public static final String DATABASER = "databaser";
    public static final String GOSSIPER = "gossiper";
    public static final String TRANSFERER = "transferer";
    
    private static final String WEIGHTER = "weighter_";
    
    public static final String getWeighterName(UniversalId linkedPeerId) {
        return WEIGHTER + linkedPeerId.toString();
    }
}
