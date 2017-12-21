package gui.scenes;

import gui.panes.WaitingPane;
import gui.utilities.GUIDimensions;
import javafx.scene.Scene;

public class FileRetrievalScene extends Scene {	
	public FileRetrievalScene(WaitingPane waitingPane) {
		super(waitingPane, GUIDimensions.mainWindowWidth, GUIDimensions.mainWindowHeight);
	}
}
