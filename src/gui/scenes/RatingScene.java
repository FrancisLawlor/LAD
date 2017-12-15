package gui.scenes;

import org.controlsfx.control.Rating;

import gui.panes.RatingPane;
import gui.utilities.GUIDimensions;
import javafx.scene.Scene;
import javafx.scene.control.Button;

public class RatingScene extends Scene {
	private RatingPane ratingsPane;
	public RatingScene(RatingPane ratingsPane) {
		super(ratingsPane, GUIDimensions.mainWindowWidth, GUIDimensions.mainWindowHeight);
		this.ratingsPane = ratingsPane;
	}
	
	public Button getSubmitButton() {
		return this.ratingsPane.getSubmitButton();
	}
	
	public Button getBackButton() {
		return this.ratingsPane.getBackButton();
	}
	
	public Rating getRating() {
		return this.ratingsPane.getRating();
	}
}
