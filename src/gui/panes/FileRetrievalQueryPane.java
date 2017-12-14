package gui.panes;

import gui.utilities.GUIText;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class FileRetrievalQueryPane extends BorderPane {
	public FileRetrievalQueryPane() {
		BorderPane fileRetrievalQuery = configureBorderPane();
		this.setCenter(fileRetrievalQuery);
	}
	
	private BorderPane configureBorderPane() {
		BorderPane content = new BorderPane();
	    content.setPadding(new Insets(5));

	    Text confirmFileRetrieval = new Text(GUIText.CONFIRMATION_FILE_RETRIEVAL);
	    Button yesButton = new Button(GUIText.CONFIRMATION_YES);
	    Button noButton = new Button(GUIText.CONFIRMATION_NO);
	    
	    content.setTop(confirmFileRetrieval);
	    	    
	    HBox buttons = new HBox();
	    buttons.setPadding(new Insets(10));
	    buttons.setSpacing(8);
	    buttons.getChildren().add(yesButton);
	    buttons.getChildren().add(noButton);
	    
	    buttons.setAlignment(Pos.CENTER);
	    content.setBottom(buttons);
	    
	    return content;
	}
}
