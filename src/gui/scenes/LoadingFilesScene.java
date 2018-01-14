package gui.scenes;

import gui.panes.WaitingPane;
import gui.utilities.GUIDimensions;
import javafx.scene.Scene;

public class LoadingFilesScene extends Scene {	
	public LoadingFilesScene(WaitingPane waitingPane) {
		super(waitingPane, GUIDimensions.mainWindowWidth, GUIDimensions.mainWindowHeight);
	}
}
