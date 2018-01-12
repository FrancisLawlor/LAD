package gui.scenes;

import content.recommend.core.Recommendation;
import gui.panes.DashBoardPane;
import gui.utilities.GUIDimensions;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

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
	
	public ListView<Recommendation> getListView() {
		return this.dashBoardPane.getListView();
	}
}
