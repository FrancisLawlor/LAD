package gui.scenes;

import gui.panes.DashBoardPane;
import javafx.scene.Scene;

public class DashBoardScene extends Scene {
	public DashBoardScene(DashBoardPane dashBoardPane) {
		super(dashBoardPane, 900, 600);
	}
}
