package statemachine.states;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;
import java.net.URISyntaxException;
import java.util.Properties;

import filemanagement.core.FileConstants;
import gui.core.GUI;
import gui.core.SceneContainerStage;
import gui.utilities.GUIText;
import statemachine.core.StateMachine;
import statemachine.utils.StateName;

public class SetupState extends State {
	private StateMachine stateMachine;
	private SceneContainerStage sceneContainerStage;
	private GUI gui;
	
	public SetupState(StateMachine stateMachine, SceneContainerStage sceneContainerStage, GUI gui) {
		this.stateMachine = stateMachine;
		this.sceneContainerStage = sceneContainerStage;
		this.gui = gui;
	}

	@Override
	public void execute(StateName param) {
		switch (param) {
			case INIT:
				initialise();
				break;
			case CLICK_SUBMIT:
				try {
					clicksSubmit();
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			default:
				break;
		}
	}
	
	private void clicksSubmit() throws IOException {
		String portNumber = gui.getSetupScene().getPortNumberTextField().getText();
		
		if (portIsAvailable(portNumber)) {
			try {
				createConfigFile();
				createFilesDirectory();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
			
			writePortNumberToConfigFile(portNumber);
			
			stateMachine.setCurrentState(StateName.RETRIEVE_RECOMMENDATIONS.toString());
			stateMachine.execute(StateName.INIT);
		} else {
			gui.getSetupScene().getErrorLabel().setText(GUIText.PORT_UNAVAILABLE);
		}
	}
	
	private void createConfigFile() throws IOException, URISyntaxException {
		FileWriter configFile = new FileWriter(FileConstants.CONFIG_FILE_NAME, true);
		String filesPath = new File(".").getAbsolutePath();
		
		Properties props = new Properties();
		props.setProperty(FileConstants.DIRECTORY_KEY, filesPath);
		props.store(configFile, FileConstants.INITIALISATION_COMMENT);
		configFile.close();
	}
	
	private void createFilesDirectory() throws IOException {
		FileReader configFile = new FileReader(FileConstants.CONFIG_FILE_NAME);
		
		Properties props = new Properties();
		props.load(configFile);
		
		String fileDirectoryPath = props.getProperty(FileConstants.DIRECTORY_KEY);
		File fileDirectory = new File(fileDirectoryPath + "/" + FileConstants.FILES_DIRECTORY_NAME);
		
		if (fileDirectory.mkdir()) {
			System.out.println(FileConstants.CREATED_FILE_DIRECTORY);
		} else {
			System.out.println(FileConstants.FAILED_TO_CREATE_FILES_DIRECTORY);
		}
	}
	
	private void writePortNumberToConfigFile(String portNumber) throws IOException {
		FileWriter configFile = new FileWriter(FileConstants.CONFIG_FILE_NAME, true);
		
		Properties props = new Properties();
		props.setProperty(FileConstants.PORT_NUMBER, portNumber);
		props.store(configFile, FileConstants.ADDED_PORTNUMBER);
		configFile.close();
	}
	
	private boolean portIsAvailable(String portNumber) {		
		try (Socket ignored = new Socket("localhost", Integer.parseInt(portNumber))) {
	        return false;
	    } catch (IOException ignored) {
	        return true;
	    }
	}
	
	private void initialise() {
		sceneContainerStage.changeScene(gui.getSetupScene());
		sceneContainerStage.setTitle(GUIText.SETUP);
	}
}
