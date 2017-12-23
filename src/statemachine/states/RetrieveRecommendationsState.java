package statemachine.states;

import java.util.concurrent.BlockingQueue;

import akka.actor.ActorRef;
import content.recommend.RecommendationsForUser;
import content.recommend.RecommendationsForUserRequest;
import core.UniversalId;
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
	private UniversalId id;
	private ActorRef viewer;
    BlockingQueue<RecommendationsForUser> recommendationsQueue;

	public RetrieveRecommendationsState(StateMachine stateMachine, SceneContainerStage sceneContainerStage, GUI gui,
	        UniversalId id, ActorRef viewer, BlockingQueue<RecommendationsForUser> queue) {
		this.stateMachine = stateMachine;
		this.sceneContainerStage = sceneContainerStage;
		this.gui = gui;
		this.viewer = viewer;
        this.recommendationsQueue = queue;
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
		
		Task<Void> sleeper = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    viewer.tell(new RecommendationsForUserRequest(id), null);
                    while (recommendationsQueue.isEmpty()) {
                        Thread.sleep(1000);
                    }
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
	}
}
