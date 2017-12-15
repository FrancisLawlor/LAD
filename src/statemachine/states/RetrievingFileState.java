package statemachine.states;

import gui.core.SceneContainerStage;
import gui.utilities.GUIText;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import statemachine.core.StateMachine;
import statemachine.utils.StateName;

public class RetrievingFileState extends State {
	StateMachine stateMachine;
	SceneContainerStage sceneContainerStage;
	
	public RetrievingFileState(StateMachine stateMachine, SceneContainerStage sceneContainerStage) {
		this.stateMachine = stateMachine;
		this.sceneContainerStage = sceneContainerStage;
	}

	@Override
	public void execute(StateName param) {
		stateMachine.setCurrentState(StateName.RETRIEVING_FILE.toString());
		sceneContainerStage.setTitle(GUIText.FILE_RETRIEVAL);


		// start downloading file from other user
		// listen for download to stop finishing
		// when download finishes change state to Rating state
		// change scene to rating scene
		
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
	            downloadFinished();
            }
        });
        new Thread(sleeper).start();
	}
	
	public void downloadFinished() {
		stateMachine.setCurrentState(StateName.RATING.toString());
		stateMachine.execute(null);
	}

}
