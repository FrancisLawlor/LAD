package tests.core;

import java.util.Iterator;

import core.PeerToPeerActor;

public abstract class DummyActor extends PeerToPeerActor {
    protected ActorTestLogger logger;
    
    protected Iterator<String> getAsynchronousLog(){
        return logger.iterator();
    }
}
