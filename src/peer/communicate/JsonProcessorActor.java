package peer.communicate;

import org.apache.camel.Processor;

import com.google.gson.Gson;

import core.PeerToPeerActor;

/**
 * Superclass for Apache Camel Processors of JSON
 *
 */
public abstract class JsonProcessorActor extends PeerToPeerActor implements Processor {
    protected static final String SUCCESS = "Message RECEIVED and Processing SUCCESSFUL at ";
    protected static final String FAIL = "Message RECEIVED but Processing FAILED at ";
    
    protected Gson gson;
    
    public JsonProcessorActor() {
        this.gson = new Gson();
    }
    
    @Override
    public void onReceive(Object object) { }
}
