package tests.content;

import tests.actors.AsynchronousLogger;

public class DummyInit {
    private AsynchronousLogger logger;
    
    public DummyInit(AsynchronousLogger logger) {
        this.logger = logger;
    }
    
    public AsynchronousLogger getLogger() {
        return this.logger;
    }
}
