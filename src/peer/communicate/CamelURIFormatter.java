package peer.communicate;

import core.UniversalId;
import core.UniversalIdResolver;

/**
 * Formats UniversalIDs to Apache Camel URIs
 * @author rory
 *
 */
public class CamelURIFormatter {
    private static final String RESTLET_COMPONENT = "restlet:";
    private static final String RESTLET_PROTOCOL = "http://";
    
    public static String getPeerRestletUri(UniversalId peerId) {
        String resolvedID = UniversalIdResolver.resolveID(peerId);
        return RESTLET_COMPONENT + RESTLET_PROTOCOL + resolvedID;
    }
}
