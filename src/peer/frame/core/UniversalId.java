package peer.frame.core;

/**
 * Universal Id for Actors
 *
 */
public class UniversalId {
    private String ipAndPort;
    
    public UniversalId(String ipAndPort) {
        this.ipAndPort = ipAndPort;
    }
    
    public String toString() {
        return this.ipAndPort;
    }
    
    @Override
    public boolean equals(Object other) {
        if (other instanceof UniversalId) {
            UniversalId otherId = (UniversalId) other;
            return this.ipAndPort.equals(otherId.ipAndPort);
        }
        else if (other instanceof String) {
            String otherId = (String) other;
            return this.ipAndPort.equals(otherId);
        }
        else {
            return this == other;
        }
    }
    
    @Override
    public int hashCode() {
        return this.ipAndPort.hashCode();
    }
}
