package gui.core;

import javafx.application.Application;
import javafx.stage.Stage;
import statemachine.core.StateMachine;
import statemachine.utils.StateName;

public class ApplicationWindow extends Application {	
    public void start(Stage stage) {
    		StateMachine stateMachine = new StateMachine();
    		
    		stateMachine.setCurrentState(StateName.START.toString());
    		
    		stateMachine.execute(null);
	}
    
    public static void main(String[] args) {
        launch(args);
    }
}