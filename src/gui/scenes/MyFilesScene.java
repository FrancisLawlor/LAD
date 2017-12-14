package gui.scenes;

import content.content.Content;
import gui.panes.MyFilesPane;
import javafx.collections.ObservableList;
import javafx.scene.Scene;

public class MyFilesScene extends Scene {
	public MyFilesScene(ObservableList<Content> data) {
		super(new MyFilesPane(data), 900, 600);
	}
}
