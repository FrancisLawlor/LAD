package tests.gui.core;

import javafx.application.Application;
import javafx.stage.Stage;
import statemachine.core.StateMachine;
import statemachine.utils.StateName;

public class TestApplicationWindow1 extends Application {
    private static TestPeerToPeerActorSystem1 p2pActorSystem;
    
    public void start(Stage stage) {
            StateMachine stateMachine = new StateMachine(p2pActorSystem);
            
            stateMachine.setCurrentState(StateName.START.toString());
            
            stateMachine.execute(null);
    }
    
    public static void main(String[] args) throws Exception {
        p2pActorSystem = new TestPeerToPeerActorSystem1();
        launch(args);
    }
}
