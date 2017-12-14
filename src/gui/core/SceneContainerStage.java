package gui.core;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneContainerStage extends Stage {
	public void init(Scene scene) {
    		this.setScene(scene);
	}
	
	public void changeScene(Scene scene) {
		this.setScene(scene);
		this.show();
	}
}
