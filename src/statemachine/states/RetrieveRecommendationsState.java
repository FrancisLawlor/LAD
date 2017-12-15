package statemachine.states;

import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import content.content.Content;
import gui.core.GUI;
import gui.core.SceneContainerStage;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import statemachine.core.StateMachine;
import statemachine.utils.StateNames;

public class RetrieveRecommendationsState extends State {
	private StateMachine stateMachine;
	private SceneContainerStage sceneContainerStage;
	private GUI gui;
	private int TIME_OUT = 10000;
	

	public RetrieveRecommendationsState(StateMachine stateMachine, SceneContainerStage sceneContainerStage, GUI gui) {
		this.stateMachine = stateMachine;
		this.sceneContainerStage = sceneContainerStage;
		this.gui = gui;
	}

	@Override
	public void execute() {
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
	            	stateMachine.setCurrentState(StateNames.DASHBOARD.toString());
	        		sceneContainerStage.changeScene(gui.getDashBoardScene());
            }
        });
        new Thread(sleeper).start();

//		Iterator<Content> recommendations;
//		try {
//			recommendations = retrieveRecommendations();
//		} catch (InterruptedException | TimeoutException e) {
//			e.printStackTrace();
//		}
		
		// Put recommendations in to observable list for Dashboard.
		
	}

	private Iterator<Content> retrieveRecommendations() throws InterruptedException, TimeoutException {
		int i = 0;
		
		Iterator<Content> recommendations = null;
		
		while (i != TIME_OUT) {
			recommendations = getRecommendations();
			if (recommendations != null) {
				break;
			} else {
	            TimeUnit.SECONDS.sleep(1);
	            i++;
			}
			if (i == TIME_OUT) {
                throw new TimeoutException("Timed out..");
                // TODO tell user it has timed out and close program.
            }
		}
		
		return recommendations;
	}
	
	private Iterator<Content> getRecommendations() {
		// TODO
		return null;
	}

}
