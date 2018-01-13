package gui.scenes;

import gui.panes.AddFilePane;
import gui.utilities.GUIDimensions;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class AddFileScene extends Scene {
	AddFilePane addFilePane;
	
	public AddFileScene(AddFilePane addFilePane) {
		super(addFilePane, GUIDimensions.mainWindowWidth, GUIDimensions.mainWindowHeight);
		this.addFilePane = addFilePane;
	}

	public TextField getFileNameTextField() {
		return this.addFilePane.getFileNameTextField();
	}
	
	public TextField getGenreTextField() {
		return this.addFilePane.getGenreTextField();
	}

	public Button getSubmitButton() {
		return this.addFilePane.getSubmitButton();
	}

	public TextField getFileFormatTextField() {
		return this.addFilePane.getFileFormatTextField();
	}

	public TextField getViewLengthTextField() {
		return this.addFilePane.getViewLengthTextField();
	}

	public TextField getYearTextField() {
		return this.addFilePane.getYearTextField();
	}

	public TextField getCreatorTextField() {
		return this.addFilePane.getCreatorTextField();
	}
}
