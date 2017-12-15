package gui.scenes;

import gui.panes.MyFilesPane;
import javafx.scene.Scene;
import javafx.scene.control.Button;

public class MyFilesScene extends Scene {
	private MyFilesPane myFilesPane;
	public MyFilesScene(MyFilesPane myFilesPane) {
		super(myFilesPane, 900, 600);
		this.myFilesPane = myFilesPane;
	}
	
	public Button getBackButton() {
		return myFilesPane.getBackButton();
	}
}
