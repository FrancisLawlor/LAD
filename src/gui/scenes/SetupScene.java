package gui.scenes;

import gui.core.SceneContainerStage;
import gui.panes.SetupPane;
import javafx.scene.Scene;

public class SetupScene extends Scene {
	public SetupScene(SceneContainerStage stage) {
		super(new SetupPane(stage), 270, 120);
	}
}
