package statemachine.states;

import gui.core.GUI;
import gui.core.SceneContainerStage;
import gui.utilities.GUIText;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import statemachine.core.StateMachine;
import statemachine.utils.StateName;

public class RetrieveRecommendationsState extends State {
	private StateMachine stateMachine;
	private SceneContainerStage sceneContainerStage;
	private GUI gui;
	

	public RetrieveRecommendationsState(StateMachine stateMachine, SceneContainerStage sceneContainerStage, GUI gui) {
		this.stateMachine = stateMachine;
		this.sceneContainerStage = sceneContainerStage;
		this.gui = gui;
	}

	@Override
	public void execute(StateName param) {
		sceneContainerStage.changeScene(gui.getRetrieveRecommendationsScene());
		sceneContainerStage.setTitle(GUIText.SETUP);

		Task<Void> sleeper = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                	
                }
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
        
        // Listen for recommendations.
        // Change state when received.
	}
	
	private void recommendationsRetrieved() {
		stateMachine.setCurrentState(StateName.DASHBOARD.toString());
    		stateMachine.execute(null);
	}
}
