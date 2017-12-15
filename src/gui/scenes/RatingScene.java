package gui.scenes;

import gui.panes.RatingPane;
import javafx.scene.Scene;

public class RatingScene extends Scene {
	public RatingScene(RatingPane ratingsPane) {
		super(ratingsPane, 900, 600);
	}
}
