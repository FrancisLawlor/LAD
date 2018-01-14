package peer.data.core;

import peer.frame.core.UniversalId;
import peer.frame.core.UniversalIdResolver;

public class Constants {
    public static final String DATA_DIR = "./data/";
    public static final String DATA_FILE_EXTENSION = ".properties";
    public static final String PEER_LINKS_FILENAME = "PeerLinks" + DATA_FILE_EXTENSION;
    public static final String SIMILAR_CONTENT_VIEW_PEERS_FILENAME = "SimilarContentViewPeers" + DATA_FILE_EXTENSION;
    public static final String CONTENT_VIEW_HISTORY_FILENAME = "ContentViewHistory" + DATA_FILE_EXTENSION;
    public static final String CONTENT_FILE_EXTENSION = ".lad";
    
    public static final String getDataDir(UniversalId peerId) {
        return DATA_DIR + UniversalIdResolver.getIdIp(peerId) + "_" + UniversalIdResolver.getIdPort(peerId) + "_data/";
    }
}
