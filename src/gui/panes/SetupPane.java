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
		
		GridPane errorMessage = configureErrorLabel();
		this.setBottom(errorMessage);
	}
	
	private GridPane configureExplanation() {
		GridPane gridPaneContainingExplanation = new GridPane();
		gridPaneContainingExplanation.setBackground(new Background(new BackgroundFill(Color.web("#e0f6f9"), CornerRadii.EMPTY, Insets.EMPTY)));
		
		// Set alignment values
	    gridPaneContainingExplanation.setAlignment(Pos.CENTER);
	    
	    Label setupInfoLabel = new Label(GUIText.SETUP_INFO);
	    setupInfoLabel.setMinWidth(100);
	    setupInfoLabel.setMinHeight(100);
	    setupInfoLabel.setFont(Font.font (15));

	    GridPane.setHalignment(setupInfoLabel, HPos.RIGHT);
	    gridPaneContainingExplanation.add(setupInfoLabel, 0, 0);
	    
		return gridPaneContainingExplanation;
	}
	
	private GridPane configureErrorLabel() {
		GridPane gridPaneContainingErrorLabel = new GridPane();
		gridPaneContainingErrorLabel.setBackground(new Background(new BackgroundFill(Color.web("#e0f6f9"), CornerRadii.EMPTY, Insets.EMPTY)));
		
		// Set alignment value
		gridPaneContainingErrorLabel.setAlignment(Pos.CENTER);
	    
	    Label portErrorLabel = new Label();
	    portErrorLabel.setTextFill(Color.RED);
	    this.errorLabel = portErrorLabel;
	    portErrorLabel.setMinWidth(100);
	    portErrorLabel.setMinHeight(100);
	    portErrorLabel.setFont(Font.font (15));

	    GridPane.setHalignment(portErrorLabel, HPos.RIGHT);
	    gridPaneContainingErrorLabel.add(portErrorLabel, 0, 0);
	    
		return gridPaneContainingErrorLabel;
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
