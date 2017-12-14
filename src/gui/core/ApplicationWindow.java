package gui.core;

import content.content.Content;
import gui.scenes.DashBoardScene;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import statemachine.core.StateMachine;
import statemachine.utils.StateNames;

public class ApplicationWindow extends Application {	
    public void start(Stage stage) {
    		StateMachine stateMachine = new StateMachine();
    		
    		stateMachine.setCurrentState(StateNames.START.toString());
    		
    		stateMachine.execute();
//    		SceneContainerStage containerStage = new SceneContainerStage();
//    		
//    		ObservableList<Content> data = FXCollections.observableArrayList();
//        data.addAll(new Content("234134", "Akira", ".mp4"), new Content("234134as", "Shakira - Shewolf", ".mp4"), new Content("31234da", "My Chemical Romance - I am Sad", ".mp3"));
//   
//    		containerStage.init(new DashBoardScene(data));
//    	
//		containerStage.show();
	}
    
    public static void main(String[] args) {
        launch(args);
    }
}