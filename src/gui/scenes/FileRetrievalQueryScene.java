package gui.scenes;

import gui.panes.FileRetrievalQueryPane;
import javafx.scene.Scene;

public class FileRetrievalQueryScene extends Scene {
	public FileRetrievalQueryScene(FileRetrievalQueryPane fileRetrievalQueryPane) {
		super(fileRetrievalQueryPane, 270, 120);
	}
}
