package peer.graph.link;

/**
 * Initialises the Peer Linker with the size of a bucket
 * Determines how big the bucket in the PeerLinkBucketor will be
 *
 */
public class PeerLinkerInit {
    private int bucketSize;
    
    public PeerLinkerInit(int bucketSize) {
        this.bucketSize = bucketSize;
    }
    
    public int getBucketSize() {
        return this.bucketSize;
    }
}
