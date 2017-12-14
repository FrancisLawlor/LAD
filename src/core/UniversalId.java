package core;

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
        return null;
    }
    
    public boolean equals(UniversalId other) {
        return this.ipAndPort.equals(other.ipAndPort);
    }
}
