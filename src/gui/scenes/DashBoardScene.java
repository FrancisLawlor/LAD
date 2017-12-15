package gui.scenes;

import gui.panes.DashBoardPane;
import javafx.scene.Scene;
import javafx.scene.control.Button;

public class DashBoardScene extends Scene {
	DashBoardPane dashBoardPane;
	
	public DashBoardScene(DashBoardPane dashBoardPane) {
		super(dashBoardPane, 900, 600);
		this.dashBoardPane = dashBoardPane;
	}
	
	public Button getMyFilesButton() {
		return this.dashBoardPane.getMyFilesButton();
	}
	
	public Button getAddFileButton() {
		return this.dashBoardPane.getAddFileButton();
	}
	
	public Button getRefreshButton() {
		return this.dashBoardPane.getRefreshButton();
	}
}
