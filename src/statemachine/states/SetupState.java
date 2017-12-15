package statemachine.states;

import gui.core.GUI;
import gui.core.SceneContainerStage;
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
		
		if (portIsAvailable("UserInput")) {
			writePortNumberToConfigFile("portNumber");
			stateMachine.setCurrentState(StateName.RETRIEVE_RECOMMENDATIONS.toString());
    			stateMachine.execute(null);
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
