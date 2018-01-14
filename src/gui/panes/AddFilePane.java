package gui.panes;

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

public class AddFilePane extends BorderPane {
	private TextField fileNameTextField;
	private TextField genreTextField;
	private TextField fileFormatTextField;
	private TextField fileViewLengthTextField;
	private TextField yearTextField;
	private TextField creatorTextField;
	private Button submitButton;
	
	public AddFilePane() {
		GridPane form = configureGridPane();
		this.setCenter(form);
	}
	
	private GridPane configureGridPane() {
		GridPane content = new GridPane();
		content.setBackground(new Background(new BackgroundFill(Color.web("#e0f6f9"), CornerRadii.EMPTY, Insets.EMPTY)));

		// Set alignment values
	    content.setAlignment(Pos.CENTER);
	    content.setPadding(new Insets(5));
	    content.setHgap(5);
	    content.setVgap(5);

	    Label fileNameLabel = new Label(GUIText.FILE_NAME);
	    TextField fileNameTextField = new TextField();
	    this.fileNameTextField = fileNameTextField;
	    
	    Label fileFormatLabel = new Label(GUIText.FILE_FORMAT);
	    TextField fileFormatTextField = new TextField();
	    this.fileFormatTextField = fileFormatTextField;
	    
	    Label genreLabel = new Label(GUIText.GENRE);
	    TextField genreTextField = new TextField();
	    this.genreTextField = genreTextField;
	    
	    Label viewLengthLabel = new Label(GUIText.VIEW_LENGTH);
	    TextField fileViewLengthTextField = new TextField();
	    this.fileViewLengthTextField = fileViewLengthTextField;
	    
	    Label yearLabel = new Label(GUIText.YEAR);
	    TextField yearTextField = new TextField();
	    this.yearTextField = yearTextField;
	    
	    Label creatorLabel = new Label(GUIText.CREATOR);
	    TextField creatorTextField = new TextField();
	    this.creatorTextField = creatorTextField;
	    
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
	    
	    GridPane.setHalignment(fileFormatLabel, HPos.RIGHT);
	    content.add(fileFormatLabel, 0, 2);
	    
	    GridPane.setHalignment(fileFormatTextField, HPos.LEFT);
	    content.add(fileFormatTextField, 1, 2);
	    
	    GridPane.setHalignment(viewLengthLabel, HPos.RIGHT);
	    content.add(viewLengthLabel, 0, 3);
	    
	    GridPane.setHalignment(fileViewLengthTextField, HPos.LEFT);
	    content.add(fileViewLengthTextField, 1, 3);
	    
	    GridPane.setHalignment(yearLabel, HPos.RIGHT);
	    content.add(yearLabel, 0, 4);
	    
	    GridPane.setHalignment(yearTextField, HPos.LEFT);
	    content.add(yearTextField, 1, 4);
	    
	    GridPane.setHalignment(creatorLabel, HPos.RIGHT);
	    content.add(creatorLabel, 0, 5);
	    
	    GridPane.setHalignment(creatorTextField, HPos.LEFT);
	    content.add(creatorTextField, 1, 5);
	    
	    GridPane.setHalignment(submitButton, HPos.RIGHT);
	    content.add(submitButton, 1, 6);
	    
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

	public TextField getFileFormatTextField() {
		return this.fileFormatTextField;
	}

	public TextField getViewLengthTextField() {
		return this.fileViewLengthTextField;
	}

	public TextField getYearTextField() {
		return this.yearTextField;
	}

	public TextField getCreatorTextField() {
		return this.creatorTextField;
	}
}
