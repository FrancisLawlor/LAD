package statemachine.states;

import content.recommend.Recommendation;
import content.recommend.RecommendationsForUser;
import gui.core.GUI;
import gui.core.SceneContainerStage;
import gui.utilities.GUIText;
import javafx.scene.control.ListView;
import peer.core.ViewerToUIChannel;
import statemachine.core.StateMachine;
import statemachine.utils.StateName;

public class DashboardState extends State {
	private StateMachine stateMachine;
	private SceneContainerStage sceneContainerStage;
	private GUI gui;
	private ViewerToUIChannel viewer;
	
	public DashboardState(StateMachine stateMachine, SceneContainerStage sceneContainerStage, GUI gui, ViewerToUIChannel viewer) {
		this.stateMachine = stateMachine;
		this.sceneContainerStage = sceneContainerStage;
		this.gui = gui;
		this.viewer = viewer;
	}

	public void execute(StateName param) {
		sceneContainerStage.changeScene(gui.getDashBoardScene());
		sceneContainerStage.setTitle(GUIText.DASHBOARD);

		switch (param) {
			case ADD_FILE:
				addFile();
				break;
			case RETRIEVE_FILE_QUERY:
				retrieveFileQuery();
				break;
			case VIEWING_FILES:
				viewFiles();
				break;
			case REFRESH:
				refresh();
				break;
			default:
				break;
		}
	}
	
	private void addFile() {
		stateMachine.setCurrentState(StateName.ADD_FILE.toString());
		stateMachine.execute(StateName.INIT);
	}
	
	private void retrieveFileQuery() {
		stateMachine.setCurrentState(StateName.RETRIEVE_FILE_QUERY.toString());
		stateMachine.execute(StateName.INIT);
	}
	
	private void viewFiles() {
		stateMachine.setCurrentState(StateName.VIEWING_FILES.toString());
		stateMachine.execute(StateName.INIT);
	}
	
	private void refresh() {
	    ListView<Recommendation> viewList = this.gui.getDashBoardScene().getListView();
	    viewList.getItems().clear();
        try {
            RecommendationsForUser recommendations = this.viewer.getRecommendations();
            for (Recommendation recommendation : recommendations) {
                viewList.getItems().add(recommendation);
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e.getCause() + e.getMessage());
        }
	}

}
