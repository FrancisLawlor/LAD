package statemachine.states;

import gui.core.GUI;
import gui.core.SceneContainerStage;
import statemachine.core.StateMachine;
import statemachine.utils.StateNames;

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
	public void execute() {
		// Create listener for button
		// Button clicked
		// Get port number from textfield
		// Check if port is available.
		
		if (portIsAvailable("UserInput")) {
			writePortNumberToConfigFile("portNumber");
			stateMachine.setCurrentState(StateNames.RETRIEVE_RECOMMENDATIONS.toString());
    			sceneContainerStage.changeScene(gui.getRetrieveRecommendationsScene());
    			stateMachine.execute();
			//Change to RetrieveRecommendationsScene
		} else {
			//Tell user port is not available
			//Throw exception.
		}
	}
	
	private boolean portIsAvailable(String portNumber) {
		return true;
	}
	
	private void writePortNumberToConfigFile(String portNumber) {
		
	}
	
}
