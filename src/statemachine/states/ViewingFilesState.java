package statemachine.states;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONArray;
import org.json.JSONObject;

import content.frame.core.Content;
import content.recommend.core.Recommendation;
import content.recommend.messages.RecommendationsForUser;
import filemanagement.core.FileConstants;
import filemanagement.core.FileHeaderKeys;
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
//        String filesJSONString = null;
//        
//		try {
//			filesJSONString = new String(Files.readAllBytes(Paths.get("./" + FileConstants.FILES_DIRECTORY_NAME + "/" + FileConstants.JSON_FILE_NAME)));
//		} catch (IOException e1) {
//			e1.printStackTrace();
//		}
//		
//		JSONArray filesJSONArray = new JSONObject(filesJSONString).getJSONArray(FileConstants.JSON_FILES_KEY);
//		
//		for (int i = 0; i < filesJSONArray.length(); i++) {
//			JSONObject object = filesJSONArray.getJSONObject(i);
//			Content content = new Content("", object.get(FileHeaderKeys.FILE_NAME).toString(), object.get(FileHeaderKeys.File_FORMAT).toString(), 0);
//			viewList.getItems().add(content);
//		}		
	//}

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
