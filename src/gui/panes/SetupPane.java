package gui.panes;

import gui.core.SceneContainerStage;
import gui.utilities.GUIText;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

public class SetupPane extends BorderPane {
	private Button nextButton;
	
	public SetupPane(SceneContainerStage stage) {
		GridPane setupForm = configureGridPane(stage);
		this.setCenter(setupForm);
	}
	
	private GridPane configureGridPane(SceneContainerStage stage) {
		GridPane content = new GridPane();
	    content.setPadding(new Insets(5));
	    content.setHgap(5);
	    content.setVgap(5);

	    Label portNumberLabel = new Label(GUIText.PORT_NUMBER);
	    TextField portNumberInputField = new TextField();
	    Button nextButton = new Button(GUIText.BUTTON_NEXT);
	    this.nextButton = nextButton;

	    GridPane.setHalignment(portNumberLabel, HPos.RIGHT);
	    content.add(portNumberLabel, 0, 0);
	    
	    GridPane.setHalignment(portNumberInputField, HPos.LEFT);
	    content.add(portNumberInputField, 1, 0);

	    GridPane.setHalignment(nextButton, HPos.RIGHT);
	    content.add(nextButton, 1, 2);
	    
	    return content;
	}

	public Button getNextButton() {
		return nextButton;
	}
}
