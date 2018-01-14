package tests.gui.core;

import gui.core.ApplicationWindow;
import javafx.stage.Stage;
import peer.frame.core.UniversalId;
import peer.frame.core.ViewerToUIChannel;
import statemachine.core.StateMachine;
import statemachine.utils.StateName;

public class TestApplicationWindow3 extends ApplicationWindow {
    private static UniversalId id;
    private static TestPeerToPeerActorSystem3 actorSystem;
    private static ViewerToUIChannel viewerChannel;
    
    public void start(Stage stage) {
            StateMachine stateMachine = new StateMachine(viewerChannel);
            
            stateMachine.setCurrentState(StateName.START.toString());
            
            stateMachine.execute(null);
    }
    
    public static void main(String[] args) throws Exception {
        id = new UniversalId("localhost:10001");
        actorSystem = new TestPeerToPeerActorSystem3(id);
        actorSystem.createActors();
        viewerChannel = actorSystem.getViewerChannel();
        actorSystem.addAFakePeer();
        
        launch(args);
    }
}
