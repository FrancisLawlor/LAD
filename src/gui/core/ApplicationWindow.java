package gui.core;

import javafx.application.Application;
import javafx.stage.Stage;
import peer.frame.core.PeerToPeerActorSystem;
import statemachine.core.StateMachine;
import statemachine.utils.StateName;

public class ApplicationWindow extends Application {
    private static PeerToPeerActorSystem p2pActorSystem;
    
    public void start(Stage stage) {
    		StateMachine stateMachine = new StateMachine(p2pActorSystem);
    		
    		stateMachine.setCurrentState(StateName.START.toString());
    		
    		stateMachine.execute(null);
	}
    
    public static void main(String[] args) throws Exception {
        p2pActorSystem = new PeerToPeerActorSystem();
        
        launch(args);
    }
}