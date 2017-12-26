package statemachine.states;

import content.recommend.Recommendation;
import content.recommend.RecommendationsForUser;
import gui.core.GUI;
import gui.core.SceneContainerStage;
import gui.utilities.GUIText;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ListView;
import peer.core.ViewerToUIChannel;
import statemachine.core.StateMachine;
import statemachine.utils.StateName;

public class RetrieveRecommendationsState extends State {
	private StateMachine stateMachine;
	private SceneContainerStage sceneContainerStage;
	private GUI gui;
	private ViewerToUIChannel viewer;

	public RetrieveRecommendationsState(StateMachine stateMachine, SceneContainerStage sceneContainerStage, GUI gui, ViewerToUIChannel viewer) {
		this.stateMachine = stateMachine;
		this.sceneContainerStage = sceneContainerStage;
		this.gui = gui;
		this.viewer = viewer;
	}

	@Override
	public void execute(StateName param) {
		switch (param) {
			case INIT:
				init();
				break;
			default:
				break;
		}
	}
	
	private void recommendationsRetrieved() {
		stateMachine.setCurrentState(StateName.DASHBOARD.toString());
    		stateMachine.execute(StateName.INIT);
	}
	
	private void init() {
		// Listen for recommendations.
        // Change state when received.
		
		sceneContainerStage.changeScene(gui.getRetrieveRecommendationsScene());
		sceneContainerStage.setTitle(GUIText.SETUP);
		
        ListView<Recommendation> viewList = this.gui.getDashBoardScene().getListView();
        viewList.getItems().clear();
		
		Task<Void> sleeper = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                RecommendationsForUser recommendations;
                try {
                    viewer.requestRecommendations();
                    recommendations = viewer.getRecommendations();
                    retrieveRecommendations(recommendations, viewList);
                    Thread.sleep(300);
                }
                catch (InterruptedException e) { }
            return null;
            }
        };
        sleeper.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
	            recommendationsRetrieved();
            }
        });
        new Thread(sleeper).start();
	}
	
	private void retrieveRecommendations(RecommendationsForUser recommendations, ListView<Recommendation> viewList) {
        for (Recommendation recommendation : recommendations) {
            viewList.getItems().add(recommendation);
        }
	}
}
