package peer.communicate;

import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

/**
 * Apache Camel Route between two RESTlet endpoints
 * Takes in two sets of options for the sender and receiver
 * Takes in a processor to process the REST according to the ActorMessage type
 *
 */
public class RESTRoute extends RouteBuilder {
    private static final String COMPONENT = "restlet:";
    private static final String PROTOCOL = "http://";
    
    private final String fromHost;
    private final String fromOptions;    
    private final String toHost;
    private final String toOptions;
    private Processor processor;
    
    /**
     * Takes in the two IPs of the sender and receiver
     * Takes in two sets of options for the sender and receiver
     * Takes in a processor to process the REST according to the ActorMessage type
     * @param fromHost
     * @param fromOptions
     * @param toHost
     * @param toOptions
     * @param processor
     */
    public RESTRoute(String fromHost, String fromOptions, String toHost,
            String toOptions, Processor processor) {
        this.fromHost = fromHost;
        this.fromOptions = fromOptions;
        this.toHost = toHost;
        this.toOptions = toOptions;
        this.processor = processor;
    }
    
    @Override
    public void configure() {
        String from_str = COMPONENT + PROTOCOL + fromHost + fromOptions;
        String to_str = COMPONENT + PROTOCOL + toHost + toOptions;
        
        from(from_str)
            .process(this.processor)
            .to(to_str);
    }
}
