package tests.gui.core;

import javafx.application.Application;
import javafx.stage.Stage;
import peer.frame.core.UniversalId;
import peer.frame.core.ViewerToUIChannel;
import statemachine.core.StateMachine;
import statemachine.utils.StateName;

public class TestApplicationWindow2 extends Application {
    private static UniversalId id;
    private static TestPeerToPeerActorSystem2 actorSystem;
    private static ViewerToUIChannel viewerChannel;
    
    public void start(Stage stage) {
            StateMachine stateMachine = new StateMachine(viewerChannel);
            
            stateMachine.setCurrentState(StateName.START.toString());
            
            stateMachine.execute(null);
    }
    
    public static void main(String[] args) throws Exception {
        id = new UniversalId("localhost:10001");
        actorSystem = new TestPeerToPeerActorSystem2(id);
        actorSystem.createActors();
        viewerChannel = actorSystem.getViewerChannel();
        actorSystem.addAFakePeer();
        
        launch(args);
    }
}
