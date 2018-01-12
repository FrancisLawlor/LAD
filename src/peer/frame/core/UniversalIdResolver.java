package peer.frame.core;

/**
 * Resolves UniversalIds to Apache Camel URIs
 *
 */
public class UniversalIdResolver {
    public static String resolveID(UniversalId id) {
        return id.toString();
    }
    
    public static String getIdIp(UniversalId id) {
        return id.toString().split(":")[0];
    }
    
    public static String getIdPort(UniversalId id) {
        return id.toString().split(":")[1];
    }
}
