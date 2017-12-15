package gui.scenes;

import gui.panes.SetupPane;
import gui.utilities.GUIDimensions;
import javafx.scene.Scene;
import javafx.scene.control.Button;

public class SetupScene extends Scene {
	private SetupPane setupPane;
	
	public SetupScene(SetupPane setupPane) {
		super(setupPane, GUIDimensions.promptWidth, GUIDimensions.promptHeight);
		this.setupPane = setupPane;
	}
	
	public Button getNextButton() {
		return setupPane.getNextButton();
	}
}
