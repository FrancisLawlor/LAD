package gui.core;

import akka.actor.ActorRef;
import core.UniversalId;
import javafx.application.Application;
import javafx.stage.Stage;
import peer.actorsystem.PeerToPeerActorSystem;
import statemachine.core.StateMachine;
import statemachine.utils.StateName;

public class ApplicationWindow extends Application {
    private static UniversalId id = new UniversalId("Test");
    private static PeerToPeerActorSystem actorSystem;
    private static ActorRef viewer;
    
    public void start(Stage stage) {
    		StateMachine stateMachine = new StateMachine(id, viewer);
    		
    		stateMachine.setCurrentState(StateName.START.toString());
    		
    		stateMachine.execute(null);
	}
    
    public static void main(String[] args) throws Exception {
        actorSystem = new PeerToPeerActorSystem(id);
        actorSystem.createActors();
        viewer = actorSystem.getViewer();
        
        launch(args);
    }
}