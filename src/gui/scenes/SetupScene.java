package gui.scenes;

import gui.panes.SetupPane;
import gui.utilities.GUIDimensions;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class SetupScene extends Scene {
	private SetupPane setupPane;
	
	public SetupScene(SetupPane setupPane) {
		super(setupPane, GUIDimensions.mainWindowWidth, GUIDimensions.mainWindowHeight);
		this.setupPane = setupPane;
	}
	
	public Button getNextButton() {
		return setupPane.getNextButton();
	}

	public TextField getPortNumberTextField() {
		return setupPane.getPortNumberTextField();
	}

	public Label getErrorLabel() {
		return setupPane.getErrorLabel();
	}
}
