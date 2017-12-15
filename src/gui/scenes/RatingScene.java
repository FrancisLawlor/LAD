package gui.scenes;

import gui.panes.RatingPane;
import javafx.scene.Scene;
import javafx.scene.control.Button;

public class RatingScene extends Scene {
	private RatingPane ratingsPane;
	public RatingScene(RatingPane ratingsPane) {
		super(ratingsPane, 900, 600);
		this.ratingsPane = ratingsPane;
	}
	
	public Button getSubmitButton() {
		return this.ratingsPane.getSubmitButton();
	}
	
	public Button getBackButton() {
		return this.ratingsPane.getBackButton();
	}
}
