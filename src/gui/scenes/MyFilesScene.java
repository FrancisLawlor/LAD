package gui.scenes;

import content.frame.core.Content;
import gui.panes.MyFilesPane;
import gui.utilities.GUIDimensions;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

public class MyFilesScene extends Scene {
	private MyFilesPane myFilesPane;
	public MyFilesScene(MyFilesPane myFilesPane) {
		super(myFilesPane, GUIDimensions.mainWindowWidth, GUIDimensions.mainWindowHeight);
		this.myFilesPane = myFilesPane;
	}
	
	public Button getBackButton() {
		return myFilesPane.getBackButton();
	}

	public ListView<Content> getFilesListView() {
		return myFilesPane.getListView();
	}
}
