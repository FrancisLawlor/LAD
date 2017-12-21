package statemachine.states;

import java.io.File;
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
		// TODO
		String portNumber = gui.getSetupScene().getPortNumberTextField().getText();
		System.out.println(portNumber);
		//if (portIsAvailable(portNumber)) {
			writePortNumberToConfigFile(portNumber);
			stateMachine.setCurrentState(StateName.RETRIEVE_RECOMMENDATIONS.toString());
			stateMachine.execute(null);
//		} else {
//			System.out.println("port is not available");
//		}
		// if port is not open/ does not exist then prompt the user to try a different number
	}
	
	private void createConfigFile() throws IOException, URISyntaxException {
		FileWriter configFile = new FileWriter(FileConstants.CONFIG_FILE_NAME);
		String filesPath = new File(".").getAbsolutePath();
		
		Properties props = new Properties();
		props.setProperty(FileConstants.DIRECTORY_KEY, filesPath);
		props.store(configFile, FileConstants.INITIALISATION_COMMENT);
	}
	
	private void writePortNumberToConfigFile(String portNumber) throws IOException {
		FileWriter configFile = new FileWriter(FileConstants.CONFIG_FILE_NAME);
		
		Properties props = new Properties();
		props.setProperty(FileConstants.PORT_NUMBER, portNumber);
		props.store(configFile, FileConstants.ADDED_PORTNUMBER);
	}
	
	private boolean portIsAvailable(String portNumber) {
		Socket socket = null;
		
		try {
			socket = new Socket("localhost", Integer.parseInt(portNumber));
			return true;
		} catch (Exception e) {
			return false;
		} finally {
			if (socket != null) {
				try {
					socket.close();
				} catch (Exception e) {
				}
			}
		}
	}
	
	private void initialise() {
		sceneContainerStage.changeScene(gui.getSetupScene());
		sceneContainerStage.setTitle(GUIText.SETUP);
		
		try {
			createConfigFile();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
}
