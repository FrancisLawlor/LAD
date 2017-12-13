package gui.scenes;

import content.content.Content;
import gui.panes.DashBoardPane;
import javafx.collections.ObservableList;
import javafx.scene.Scene;

public class DashBoardScene extends Scene {
	public DashBoardScene(ObservableList<Content> data) {
		super(new DashBoardPane(data), 900, 600);
	}
}
