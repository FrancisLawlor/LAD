package statemachine.states;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
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
		sceneContainerStage.changeScene(gui.getSetupScene());
		sceneContainerStage.setTitle(GUIText.SETUP);
		
		try {
			createConfigFile();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		
		switch (param) {
			case CLICK_SUBMIT:
				clicksSubmit();
				break;
			default:
				break;
		}
	}
	
	private void clicksSubmit() {
		// TODO
		// Gets Number from TextField
		String portNumber = gui.getSetupScene().getPortNumberTextField().getText();
		System.out.println(portNumber);
		// Checks if port is open using static object
		// If port is open write port number to config file
		// change to dashboard
		stateMachine.setCurrentState(StateName.RETRIEVE_RECOMMENDATIONS.toString());
		stateMachine.execute(null);
		// if port is not open/ does not exist then prompt the user to try a different number
	}
	
	private void createConfigFile() throws IOException, URISyntaxException {
		FileWriter configFile = new FileWriter(FileConstants.CONFIG_FILE_NAME);
		String filesPath = AddFileState.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
		//System.out.println("FilePath: " + filesPath);
		
		Properties props = new Properties();
		props.setProperty("filesDirectory", filesPath);
		props.store(configFile, "File initialised.");
	}
}
