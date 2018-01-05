package tests.core;

public class DummyInit {
    private tests.core.ActorTestLogger logger;
    
    public DummyInit(tests.core.ActorTestLogger logger) {
        this.logger = logger;
    }
    
    public tests.core.ActorTestLogger getLogger() {
        return this.logger;
    }
}
