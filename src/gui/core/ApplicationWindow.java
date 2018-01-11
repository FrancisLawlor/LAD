package gui.core;

import javafx.application.Application;
import javafx.stage.Stage;
import peer.core.PeerToPeerActorSystem;
import peer.core.UniversalId;
import peer.core.ViewerToUIChannel;
import statemachine.core.StateMachine;
import statemachine.utils.StateName;

public class ApplicationWindow extends Application {
    private static final String ADDRESS = "localhost:10001";
    private static PeerToPeerActorSystem actorSystem;
    private static ViewerToUIChannel viewerChannel;
    
    public void start(Stage stage) {
    		StateMachine stateMachine = new StateMachine(viewerChannel);
    		
    		stateMachine.setCurrentState(StateName.START.toString());
    		
    		stateMachine.execute(null);
	}
    
    public static void main(String[] args) throws Exception {
        UniversalId id = new UniversalId(ADDRESS);
        actorSystem = new PeerToPeerActorSystem(id);
        actorSystem.createActors();
        viewerChannel = actorSystem.getViewerChannel();
        
        launch(args);
    }
}