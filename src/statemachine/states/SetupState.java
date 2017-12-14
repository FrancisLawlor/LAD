package statemachine.states;

import statemachine.core.StateMachine;
import statemachine.utils.StateNames;

public class SetupState extends State {
	private StateMachine stateMachine;
	
	public SetupState(StateMachine stateMachine) {
		super(stateMachine);
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
