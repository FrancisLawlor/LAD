package gui.panes;

import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class WaitingPane extends BorderPane {	
	public WaitingPane(String waitingMessage) {
		StackPane waitingContent = configureStackPane(waitingMessage);
		
		this.setCenter(waitingContent);
	}

	private StackPane configureStackPane(String waitingMessage) {
		StackPane content = new StackPane();
		ProgressIndicator progressIndicator = new ProgressIndicator();
		progressIndicator.setMaxSize(150, 150);
	    	content.getChildren().add(progressIndicator);
	    	content.getChildren().add(new Text(waitingMessage));
	    	
	    	return content;
	}
}
