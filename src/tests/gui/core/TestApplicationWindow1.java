package tests.gui.core;

import javafx.application.Application;
import javafx.stage.Stage;
import peer.core.UniversalId;
import peer.core.ViewerToUIChannel;
import statemachine.core.StateMachine;
import statemachine.utils.StateName;

public class TestApplicationWindow1 extends Application {
    private static UniversalId id;
    private static TestPeerToPeerActorSystem1 actorSystem;
    private static ViewerToUIChannel viewerChannel;
    
    public void start(Stage stage) {
            StateMachine stateMachine = new StateMachine(viewerChannel);
            
            stateMachine.setCurrentState(StateName.START.toString());
            
            stateMachine.execute(null);
    }
    
    public static void main(String[] args) throws Exception {
        id = new UniversalId("localhost:10001");
        actorSystem = new TestPeerToPeerActorSystem1(id);
        actorSystem.createActors();
        viewerChannel = actorSystem.getViewerChannel();
        
        launch(args);
    }
}
