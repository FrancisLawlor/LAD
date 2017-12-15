package gui.scenes;

import gui.panes.DashBoardPane;
import gui.utilities.GUIDimensions;
import javafx.scene.Scene;
import javafx.scene.control.Button;

public class DashBoardScene extends Scene {
	DashBoardPane dashBoardPane;
	
	public DashBoardScene(DashBoardPane dashBoardPane) {
		super(dashBoardPane, GUIDimensions.mainWindowWidth, GUIDimensions.mainWindowHeight);
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
