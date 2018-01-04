package gui.panes;

import gui.utilities.GUIText;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class FileRetrievalQueryPane extends BorderPane {
	private Button yesButton;
	private Button noButton;

	public FileRetrievalQueryPane() {
		GridPane infoMessage = configureExplanation();
		this.setTop(infoMessage);

		GridPane inputButtons = configureInput();
		this.setCenter(inputButtons);
	}
	
	private GridPane configureExplanation() {
		GridPane gridPaneContainingExplanation = new GridPane();
		gridPaneContainingExplanation.setBackground(new Background(new BackgroundFill(Color.web("#e0f6f9"), CornerRadii.EMPTY, Insets.EMPTY)));
		
		// Set alignment values
	    gridPaneContainingExplanation.setAlignment(Pos.CENTER);
	    
	    Label portNumberLabel = new Label(GUIText.CONFIRMATION_FILE_RETRIEVAL);
	    portNumberLabel.setMinWidth(100);
	    portNumberLabel.setMinHeight(100);
	    portNumberLabel.setFont(Font.font (15));

	    GridPane.setHalignment(portNumberLabel, HPos.RIGHT);
	    gridPaneContainingExplanation.add(portNumberLabel, 0, 0);
	    
		return gridPaneContainingExplanation;
	}
	
	private GridPane configureInput() {
		GridPane content = new GridPane();
		content.setBackground(new Background(new BackgroundFill(Color.web("#e0f6f9"), CornerRadii.EMPTY, Insets.EMPTY)));

		// Set alignment values
		content.setAlignment(Pos.CENTER);
	    content.setPadding(new Insets(5));
	    content.setHgap(10);
	    
	    // Create yes button
	    Button yesButton = new Button(GUIText.CONFIRMATION_YES);
	    this.yesButton = yesButton;
	    
	    GridPane.setHalignment(yesButton, HPos.RIGHT);
	    content.add(yesButton, 0, 0);
	    yesButton.setMinHeight(40);
	    yesButton.setMinWidth(75);
	    
	    // Create no button
	    Button noButton = new Button(GUIText.CONFIRMATION_NO);
	    this.noButton = noButton;
	    
	    GridPane.setHalignment(noButton, HPos.LEFT);
	    content.add(noButton, 1, 0);
	    noButton.setMinHeight(40);
	    noButton.setMinWidth(75);
	    
	    return content;
	}
	
	public Button getYesButton() {
		return yesButton;
	}

	public Button getNoButton() {
		return noButton;
	}
}
