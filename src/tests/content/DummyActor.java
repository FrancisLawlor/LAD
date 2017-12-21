package tests.content;

import java.util.Iterator;

import core.PeerToPeerActor;
import tests.actors.AsynchronousLogger;

public abstract class DummyActor extends PeerToPeerActor {
    protected AsynchronousLogger logger;
    
    protected Iterator<String> getAsynchronousLog(){
        return logger.iterator();
    }
}
