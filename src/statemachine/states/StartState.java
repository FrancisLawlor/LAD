package statemachine.states;

import statemachine.core.StateMachine;
import statemachine.utils.StateNames;

public class StartState extends State {
	private StateMachine stateMachine;
	
	public StartState(StateMachine stateMachine) {
		super(stateMachine);
	}

	@Override
	public void execute() {
		if (!configFileExists()) {
			stateMachine.setCurrentState(StateNames.SETUP.toString());
			//Change to Setup scene
		} else {
			stateMachine.setCurrentState(StateNames.RETRIEVE_RECOMMENDATIONS.toString());
			//Change to Retrieve Recommendations Scene
		}
	}
	
	// TODO
	private boolean configFileExists() {
		return true;
	}

}
