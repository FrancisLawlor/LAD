package tests.core;

import java.util.Iterator;

import core.PeerToPeerActor;

public abstract class DummyActor extends PeerToPeerActor {
    protected AsynchronousLogger logger;
    
    protected Iterator<String> getAsynchronousLog(){
        return logger.iterator();
    }
}
