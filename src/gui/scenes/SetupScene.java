package gui.scenes;

import gui.panes.SetupPane;
import javafx.scene.Scene;
import javafx.scene.control.Button;

public class SetupScene extends Scene {
	private SetupPane setupPane;
	
	public SetupScene(SetupPane setupPane) {
		super(setupPane, 270, 120);
		this.setupPane = setupPane;
	}
	
	public Button getNextButton() {
		return setupPane.getNextButton();
	}
}
