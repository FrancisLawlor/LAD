package gui.scenes;

import gui.panes.WaitingPane;
import gui.utilities.GUIText;
import javafx.scene.Scene;

public class FileRetrievalScene extends Scene {

	public FileRetrievalScene() {
		super(new WaitingPane(GUIText.RETRIEVING_FILE), 600, 900);
	}
}
