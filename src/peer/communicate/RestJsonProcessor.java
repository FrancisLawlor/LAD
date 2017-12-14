package peer.communicate;

import org.apache.camel.Processor;

import com.google.gson.Gson;

public abstract class RestJsonProcessor implements Processor {
    protected Gson gson = new Gson();
    
    
}
