package gui.panes;

import gui.utilities.GUIText;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

public class AddFilePane extends BorderPane {
	private TextField fileNameTextField;
	private TextField genreTextField;
	private Button submitButton;
	
	public AddFilePane() {
		GridPane form = configureGridPane();
		this.setLeft(form);
	}
	
	private GridPane configureGridPane() {
		GridPane content = new GridPane();
	    content.setPadding(new Insets(5));
	    content.setHgap(5);
	    content.setVgap(5);

	    Label fileNameLabel = new Label(GUIText.FILE_NAME);
	    TextField fileNameTextField = new TextField();
	    this.fileNameTextField = fileNameTextField;
	    
	    Label genreLabel = new Label(GUIText.GENRE);
	    TextField genreTextField = new TextField();
	    this.genreTextField = genreTextField;
	    
	    Button submitButton = new Button(GUIText.SUBMIT);
	    this.submitButton = submitButton;

	    GridPane.setHalignment(fileNameLabel, HPos.RIGHT);
	    content.add(fileNameLabel, 0, 0);
	    
	    GridPane.setHalignment(fileNameTextField, HPos.LEFT);
	    content.add(fileNameTextField, 1, 0);
	    
	    GridPane.setHalignment(genreLabel, HPos.RIGHT);
	    content.add(genreLabel, 0, 1);
	    
	    GridPane.setHalignment(genreTextField, HPos.LEFT);
	    content.add(genreTextField, 1, 1);

	    GridPane.setHalignment(submitButton, HPos.RIGHT);
	    content.add(submitButton, 1, 2);
	    
	    return content;
	}

	public TextField getFileNameTextField() {
		return this.fileNameTextField;
	}

	public TextField getGenreTextField() {
		return this.genreTextField;
	}
	
	public Button getSubmitButton() {
		return this.submitButton;
	}
}
