package gui.scenes;

import gui.panes.WaitingPane;
import gui.utilities.GUIDimensions;
import javafx.scene.Scene;

public class RetrieveRecommendationsScene extends Scene {
	public RetrieveRecommendationsScene(WaitingPane waitingPane) {
		super(waitingPane, GUIDimensions.mainWindowWidth, GUIDimensions.mainWindowHeight);
	}
}
