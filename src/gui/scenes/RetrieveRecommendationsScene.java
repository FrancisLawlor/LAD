package gui.scenes;

import gui.panes.WaitingPane;
import javafx.scene.Scene;

public class RetrieveRecommendationsScene extends Scene {
	public RetrieveRecommendationsScene(WaitingPane waitingPane) {
		super(waitingPane, 600, 900);
	}
}
