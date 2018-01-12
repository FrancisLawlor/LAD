package statemachine.states;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONArray;
import org.json.JSONObject;

import content.frame.core.Content;
import filemanagement.core.FileConstants;
import filemanagement.core.FileHeaderKeys;
import gui.core.GUI;
import gui.core.SceneContainerStage;
import gui.utilities.GUIText;
import javafx.scene.control.ListView;
import statemachine.core.StateMachine;
import statemachine.utils.StateName;

public class ViewingFilesState extends State {
	StateMachine stateMachine;
	SceneContainerStage sceneContainerStage;
	GUI gui;
	
	public ViewingFilesState(StateMachine stateMachine, SceneContainerStage sceneContainerStage, GUI gui) {
		this.stateMachine = stateMachine;
		this.sceneContainerStage = sceneContainerStage;
		this.gui = gui;
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
        String filesJSONString = null;
        
		try {
			filesJSONString = new String (Files.readAllBytes(Paths.get("./" + FileConstants.JSON_FILE_NAME)));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		JSONArray filesJSONArray = new JSONObject(filesJSONString).getJSONArray(FileConstants.JSON_FILES_KEY);
		
		for (int i = 0; i < filesJSONArray.length(); i++) {
			JSONObject object = filesJSONArray.getJSONObject(i);
			Content content = new Content("", object.get(FileHeaderKeys.FILE_NAME).toString(), object.get(FileHeaderKeys.File_FORMAT).toString(), 0);
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
