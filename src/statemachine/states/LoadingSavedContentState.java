package statemachine.states;

import content.frame.core.Content;
import gui.core.GUI;
import gui.core.SceneContainerStage;
import gui.utilities.GUIText;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ListView;
import peer.data.messages.LocalSavedContentResponse;
import peer.frame.core.PeerToPeerActorSystem;
import peer.frame.core.ViewerToUIChannel;
import statemachine.core.StateMachine;
import statemachine.utils.StateName;

public class LoadingSavedContentState extends State {
	private StateMachine stateMachine;
	private SceneContainerStage sceneContainerStage;
	private GUI gui;
	private ViewerToUIChannel viewer;
	private PeerToPeerActorSystem p2pActorSystem;

	public LoadingSavedContentState(StateMachine stateMachine, SceneContainerStage sceneContainerStage, GUI gui, PeerToPeerActorSystem p2pActorSystem) {
		this.stateMachine = stateMachine;
		this.sceneContainerStage = sceneContainerStage;
		this.gui = gui;
		this.p2pActorSystem = p2pActorSystem;
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
	
	private void init() {
		this.viewer = this.p2pActorSystem.getViewerChannel();
		sceneContainerStage.changeScene(gui.getLoadingSavedContentScene());
		sceneContainerStage.setTitle(GUIText.LOADING_SAVED_FILES);	
	
		ListView<Content> viewList = this.gui.getMyFilesScene().getFilesListView();
		viewList.getItems().clear();
		
		Task<Void> sleeper = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				LocalSavedContentResponse contents;
				try {
					viewer.requestSavedContent();
					contents = viewer.getSavedContent();
					retrieveContents(contents, viewList);
					Thread.sleep(300);
				} catch (InterruptedException e) {
					
				}
				
				return null;
			}
		};
		sleeper.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent event) {
				contentsRetrieved();
			}
		});
		new Thread(sleeper).start();
	}
	
	private void contentsRetrieved() {
		stateMachine.setCurrentState(StateName.VIEWING_FILES.toString());
    		stateMachine.execute(StateName.INIT);
	}
	
	private void retrieveContents(LocalSavedContentResponse contents, ListView<Content> viewList) {
        for (Content content : contents) {
            viewList.getItems().add(content);
        }
	}
}
