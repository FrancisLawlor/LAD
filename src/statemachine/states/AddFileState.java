package statemachine.states;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;

import content.frame.core.Content;
import content.frame.core.MediaAttributes;
import content.view.core.ContentViews;
import filemanagement.core.FileConstants;
import filemanagement.filewrapper.FileWrapper;
import gui.core.GUI;
import gui.core.SceneContainerStage;
import gui.utilities.GUIText;
import javafx.stage.FileChooser;
import statemachine.core.StateMachine;
import statemachine.utils.StateName;

public class AddFileState extends State {
	private StateMachine stateMachine;
	private SceneContainerStage sceneContainerStage;
	private GUI gui;
	private File file;
	
	public AddFileState(StateMachine stateMachine, SceneContainerStage sceneContainerStage, GUI gui) {
		this.stateMachine = stateMachine;
		this.sceneContainerStage = sceneContainerStage;
		this.gui = gui;
	}

	@Override
	public void execute(StateName param) {
		switch (param) {
			case INIT:
				sceneContainerStage.changeScene(gui.getAddFileScene());
				sceneContainerStage.setTitle(GUIText.ADD_FILE);
				
				file = chooseFile();
				
				if (file == null) {
					stateMachine.setCurrentState(StateName.DASHBOARD.toString());
					stateMachine.execute(StateName.INIT);
				}
				break;
			case CLICK_SUBMIT:
				writeInfoToFile();
				try {
					storeFileInformation();
				} catch (IOException e) {
					e.printStackTrace();
				}
				stateMachine.setCurrentState(StateName.DASHBOARD.toString());
				stateMachine.execute(StateName.INIT);
				break;
			default:
				break;
		}
	}
	
	private File chooseFile() {		
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle(GUIText.SELECT_FILE);
		File file = fileChooser.showOpenDialog(sceneContainerStage);
		
        if (file != null) {
            return file;
        }
		
        return null;		
	}
	
	private void writeInfoToFile() {
		String jsonData = writeJSONHeader();
		
		try {
			byte[] wrappedFile = FileWrapper.mergeHeaderDataWithMediaFile(jsonData.getBytes(), file.getAbsolutePath());
						
			FileReader configFile = new FileReader(FileConstants.CONFIG_FILE_NAME);
			Properties props = new Properties();
			props.load(configFile);
			
			String wrappedFileOutputPath = props.getProperty(FileConstants.DIRECTORY_KEY) + "/" +
					gui.getAddFileScene().getFileNameTextField().getText() + FileConstants.WRAPPED_FILE_EXTENSION;
			
			try (FileOutputStream fileOutputStream = new FileOutputStream(wrappedFileOutputPath)) {
				fileOutputStream.write(wrappedFile);
				fileOutputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}	
	}
	
	private String writeJSONHeader() {
		ContentViews contentViews = new ContentViews(
				new Content("todo", gui.getAddFileScene().getFileNameTextField().getText(), 
						gui.getAddFileScene().getFileFormatTextField().getText(), 
						Integer.parseInt(gui.getAddFileScene().getViewLengthTextField().getText()), 
							new MediaAttributes(gui.getAddFileScene().getGenreTextField().getText(), 
									gui.getAddFileScene().getYearTextField().getText(),
									gui.getAddFileScene().getCreatorTextField().getText())));
		
		Gson gsonUtil = new Gson();
		
		return gsonUtil.toJson(contentViews);
	}
	
	private void storeFileInformation() throws IOException {
		String filesJSONString = new String (Files.readAllBytes(Paths.get("./" + FileConstants.FILES_DIRECTORY_NAME + "/" + FileConstants.JSON_FILE_NAME)));
		
		JSONObject filesJSONObject = new JSONObject(filesJSONString);
		Gson gsonUtil = new Gson();
		
		((JSONArray) filesJSONObject.get(FileConstants.JSON_FILES_KEY))
			.put(new JSONObject(gsonUtil.toJson(new Content("todo", gui.getAddFileScene().getFileNameTextField().getText(), 
						gui.getAddFileScene().getFileFormatTextField().getText(), 
						Integer.parseInt(gui.getAddFileScene().getViewLengthTextField().getText()), 
							new MediaAttributes(gui.getAddFileScene().getGenreTextField().getText(), 
									gui.getAddFileScene().getYearTextField().getText(),
									gui.getAddFileScene().getCreatorTextField().getText()))))
			);
		
		File jsonFile = new File("./" + FileConstants.FILES_DIRECTORY_NAME + "/" + FileConstants.JSON_FILE_NAME);
		
		try {
			FileWriter fileWriter = new FileWriter(jsonFile, false);
			fileWriter.write(filesJSONObject.toString());
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
