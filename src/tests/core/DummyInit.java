package tests.core;

public class DummyInit {
    private ActorTestLogger logger;
    
    public DummyInit(ActorTestLogger logger) {
        this.logger = logger;
    }
    
    public ActorTestLogger getLogger() {
        return this.logger;
    }
}
