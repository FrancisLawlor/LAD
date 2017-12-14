package gui.panes;

import org.controlsfx.control.Rating;

import gui.utilities.GUIText;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class RatingPane extends BorderPane {
	public RatingPane() {
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
        content.getChildren().add(backButton);
	    
	    return content;
	}
	
	private GridPane configureGridPane() {
		GridPane content = new GridPane();
	    content.setPadding(new Insets(5));

	    final Rating rating = new Rating();
	    content.add(rating, 0, 0);
	    content.setAlignment(Pos.CENTER);
	    
	    return content;
	}
	
	private BorderPane configureRightBar() {
		BorderPane content = new BorderPane();
	    content.setPadding(new Insets(5));
		
		Button submitButton = new Button(GUIText.SUBMIT);
		content.setBottom(submitButton);
		
		return content;
	}
}
