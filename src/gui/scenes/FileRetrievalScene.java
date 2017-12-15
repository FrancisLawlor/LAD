package gui.scenes;

import gui.panes.WaitingPane;
import javafx.scene.Scene;

public class FileRetrievalScene extends Scene {

	public FileRetrievalScene(WaitingPane waitingPane) {
		super(waitingPane, 600, 900);
	}
}
