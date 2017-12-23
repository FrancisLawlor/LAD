package tests.gui.core;

import akka.actor.ActorRef;
import core.UniversalId;
import javafx.application.Application;
import javafx.stage.Stage;
import statemachine.core.StateMachine;
import statemachine.utils.StateName;

public class TestApplicationWindow1 extends Application {
    private static UniversalId id;
    private static TestPeerToPeerActorSystem1 actorSystem;
    private static ActorRef viewer;
    
    public void start(Stage stage) {
            StateMachine stateMachine = new StateMachine(id, viewer);
            
            stateMachine.setCurrentState(StateName.START.toString());
            
            stateMachine.execute(null);
    }
    
    public static void main(String[] args) throws Exception {
        id = new UniversalId("localhost:10001");
        actorSystem = new TestPeerToPeerActorSystem1(id);
        actorSystem.createActors();
        viewer = actorSystem.getViewer();
        
        launch(args);
    }
}
