package core;

/**
 * Resolves UniversalIds to Apache Camel URIs
 *
 */
public class UniversalIdResolver {
    public static String resolveID(UniversalId id) {
        return id.toString();
    }
}
