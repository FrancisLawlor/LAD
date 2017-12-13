package gui.core;

import gui.scenes.SetupScene;
import javafx.application.Application;
import javafx.stage.Stage;

public class ApplicationWindow extends Application {	
    public void start(Stage stage) {
    		SceneContainerStage containerStage = new SceneContainerStage();
    		
    		containerStage.init(new SetupScene(containerStage));
    	
		containerStage.show();
	}
}