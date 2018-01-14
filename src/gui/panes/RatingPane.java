package gui.panes;

import org.controlsfx.control.Rating;

import gui.utilities.GUIText;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class RatingPane extends BorderPane {
	private Button submitButton;
	private Button backButton;
	private Rating rating;
	
	public RatingPane() {
		this.setBackground(new Background(new BackgroundFill(Color.web("#e0f6f9"), CornerRadii.EMPTY, Insets.EMPTY)));

		VBox leftBar = configureLeftBar();
		this.setLeft(leftBar);
		
		GridPane rating = configureGridPane();
		this.setCenter(rating);
		
		BorderPane rightBar = configureRightBar();
		this.setRight(rightBar);
	}
	
	private VBox configureLeftBar() {
		VBox content = new VBox();
	    content.setPadding(new Insets(10));
	    content.setSpacing(8);
	    
	    Button backButton = new Button(GUIText.BACK_BUTTON);
	    this.backButton = backButton;
	    
        content.getChildren().add(backButton);
	    
	    return content;
	}
	
	private GridPane configureGridPane() {
		GridPane content = new GridPane();
	    content.setPadding(new Insets(5));

	    final Rating rating = new Rating();
	    this.rating = rating;
	    
	    content.add(rating, 0, 0);
	    content.setAlignment(Pos.CENTER);
	    
	    return content;
	}
	
	private BorderPane configureRightBar() {
		BorderPane content = new BorderPane();
	    content.setPadding(new Insets(5));
		
		Button submitButton = new Button(GUIText.SUBMIT);
		this.submitButton = submitButton;
		
		content.setBottom(submitButton);
		
		return content;
	}
	
	public Button getSubmitButton() {
		return this.submitButton;
	}

	public Button getBackButton() {
		return this.backButton;
	}
	
	public Rating getRating() {
		return this.rating;
	}
}
