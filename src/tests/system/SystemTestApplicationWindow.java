package tests.system;

import javafx.application.Application;
import javafx.stage.Stage;
import statemachine.core.StateMachine;
import statemachine.utils.StateName;

public class SystemTestApplicationWindow extends Application {
    private static PeerOne peerOne;
    
    public void start(Stage stage) {
            StateMachine stateMachine = new StateMachine(peerOne);
            
            stateMachine.setCurrentState(StateName.START.toString());
            
            stateMachine.execute(null);
    }
    
    public static void main(String[] args) throws Exception {
        peerOne = new PeerOne();
        
        launch(args);
    }
}
