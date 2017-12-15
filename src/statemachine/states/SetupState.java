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
		
		clicksSubmit();
	}
	
	private void clicksSubmit() {
		// TODO
		// Gets Number from TextField
		String portNumber = gui.getSetupScene().getPortNumberTextField().getText();
		System.out.println(portNumber);
		// Checks if port is open using static object
		// If port is open write port number to config file 
		// change to dashboard
		stateMachine.setCurrentState(StateName.DASHBOARD.toString());
		stateMachine.execute(null);
		// if port is not open/ does not exist then prompt the user to try a different number
	}
}
