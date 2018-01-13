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
import peer.frame.core.ViewerToUIChannel;
import statemachine.core.StateMachine;
import statemachine.utils.StateName;

public class ViewingFilesState extends State {
	private StateMachine stateMachine;
	private SceneContainerStage sceneContainerStage;
	private GUI gui;
	private ViewerToUIChannel viewer;
	
	public ViewingFilesState(StateMachine stateMachine, SceneContainerStage sceneContainerStage, GUI gui, ViewerToUIChannel viewer) {
		this.stateMachine = stateMachine;
		this.sceneContainerStage = sceneContainerStage;
		this.gui = gui;
		this.viewer = viewer;
	}

	@Override
	public void execute(StateName param) {
		sceneContainerStage.changeScene(gui.getMyFilesScene());
		sceneContainerStage.setTitle(GUIText.MY_FILES);
		
		populateListView();
		
		switch (param) {
			case CLICK_BACK:
				clicksBack();
				break;
			case CLICK_FILE:
				clicksFile();
				break;
			default:
				break;
			}
	}
	
	private void populateListView() {
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
				} catch (InterruptedException e) { }
				
				return null;
			}
		};
		sleeper.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent event) {
				contentsRetrieved();
			}

			private void contentsRetrieved() {
				System.out.println("Contents retrieved.");
			}
		});
		new Thread(sleeper).start();
	}
	
	private void retrieveContents(LocalSavedContentResponse contents, ListView<Content> viewList) {
        for (Content content : contents) {
            viewList.getItems().add(content);
        }
	}

	private void clicksBack() {
		stateMachine.setCurrentState(StateName.DASHBOARD.toString());
		stateMachine.execute(StateName.INIT);
	}
	
	private void clicksFile() {
		// TODO
		// file opens
		// change to rating state
		// change to rating scene
	}

}
