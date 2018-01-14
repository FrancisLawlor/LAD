package tests.gui.core;

import gui.core.ApplicationWindow;
import javafx.stage.Stage;
import statemachine.core.StateMachine;
import statemachine.utils.StateName;

public class TestApplicationWindow3 extends ApplicationWindow {
    private static TestPeerToPeerActorSystem3 p2pActorSystem;
    
    public void start(Stage stage) {
            StateMachine stateMachine = new StateMachine(p2pActorSystem);
            
            stateMachine.setCurrentState(StateName.START.toString());
            
            stateMachine.execute(null);
    }
    
    public static void main(String[] args) throws Exception {
        p2pActorSystem = new TestPeerToPeerActorSystem3();
        launch(args);
    }
}
