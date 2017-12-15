package gui.scenes;

import gui.panes.MyFilesPane;
import javafx.scene.Scene;

public class MyFilesScene extends Scene {
	public MyFilesScene(MyFilesPane myFilesPane) {
		super(myFilesPane, 900, 600);
	}
}
