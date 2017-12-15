package gui.scenes;

import gui.panes.FileRetrievalQueryPane;
import javafx.scene.Scene;
import javafx.scene.control.Button;

public class FileRetrievalQueryScene extends Scene {
	private FileRetrievalQueryPane fileRetrievalQueryPane;
	public FileRetrievalQueryScene(FileRetrievalQueryPane fileRetrievalQueryPane) {
		super(fileRetrievalQueryPane, 270, 120);
		this.fileRetrievalQueryPane = fileRetrievalQueryPane;
	}
	
	public Button getYesButton() {
		return fileRetrievalQueryPane.getYesButton();
	}

	public Button getNoButton() {
		return fileRetrievalQueryPane.getNoButton();
	}
}
