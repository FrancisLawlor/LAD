package gui.panes;

import gui.core.SceneContainerStage;
import gui.utilities.GUIText;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class SetupPane extends BorderPane {
	private Button nextButton;
	private TextField portNumberTextField;
	private Label errorLabel;
	
	public SetupPane(SceneContainerStage stage) {
		GridPane infoMessage = configureExplanation();
		this.setTop(infoMessage);
		
		GridPane setupForm = configureInput();
		this.setCenter(setupForm);
	}
	
	private GridPane configureExplanation() {
		GridPane gridPaneContainingExplanation = new GridPane();
		gridPaneContainingExplanation.setBackground(new Background(new BackgroundFill(Color.web("#e0f6f9"), CornerRadii.EMPTY, Insets.EMPTY)));
		
		// Set alignment values
	    gridPaneContainingExplanation.setAlignment(Pos.CENTER);
	    
	    Label portNumberLabel = new Label(GUIText.SETUP_INFO);
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
	    content.setHgap(5);
	    content.setVgap(5);
	    
	    // Create port number text field and label.
	    Label portNumberLabel = new Label(GUIText.PORT_NUMBER);
	    TextField portNumberTextField = new TextField();
	    this.portNumberTextField = portNumberTextField;
	    
	    GridPane.setHalignment(portNumberLabel, HPos.RIGHT);
	    content.add(portNumberLabel, 0, 0);
	    
	    GridPane.setHalignment(portNumberTextField, HPos.LEFT);
	    content.add(portNumberTextField, 1, 0);
	    
	    // Create next button
	    Button nextButton = new Button(GUIText.BUTTON_NEXT);
	    this.nextButton = nextButton;

	    GridPane.setHalignment(nextButton, HPos.RIGHT);
	    content.add(nextButton, 1, 2);
	    
	    return content;
	}

	public Button getNextButton() {
		return this.nextButton;
	}

	public TextField getPortNumberTextField() {
		return this.portNumberTextField;
	}

	public Label getErrorLabel() {
		return this.errorLabel;
	}
}
