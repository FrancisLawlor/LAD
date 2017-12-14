package gui.scenes;

import gui.panes.WaitingPane;
import gui.utilities.GUIText;
import javafx.scene.Scene;

public class RetrieveRecommendationsScene extends Scene {
	public RetrieveRecommendationsScene() {
		super(new WaitingPane(GUIText.FINDING_RECOMMENDATIONS), 600, 900);
	}
}
