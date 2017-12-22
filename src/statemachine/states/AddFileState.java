package statemachine.states;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.json.JSONObject;

import filemanagement.core.FileConstants;
import filemanagement.core.FileHeaderKeys;
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
				break;
			case CLICK_SUBMIT:
				writeInfoToFile();
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
		String jsonData = writeJSON();
		
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
		
		stateMachine.setCurrentState(StateName.DASHBOARD.toString());
		stateMachine.execute(StateName.INIT);
	}
	
	private String writeJSON() {
		return new JSONObject()
				.put(FileHeaderKeys.CONTENT, new JSONObject()
						.put(FileHeaderKeys.FILE_NAME, file.getName())
						.put(FileHeaderKeys.FILE_GENRE, gui.getAddFileScene().getGenreTextField().getText()
                    		 )).toString();
	}
}
