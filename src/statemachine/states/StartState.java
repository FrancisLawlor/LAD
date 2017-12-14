package statemachine.states;

import statemachine.core.StateMachine;
import statemachine.utils.StateNames;

public class StartState extends State {
	private StateMachine stateMachine;
	
	StartState(StateMachine stateMachine) {
		super(stateMachine);
	}

	@Override
	public void execute() {
		if (!configFileExists()) {
			stateMachine.setCurrentState(StateNames.SETUP.toString());
		} else {
			stateMachine.setCurrentState(StateNames.RETRIEVE_RECOMMENDATIONS.toString());
		}
	}
	
	// TODO
	private boolean configFileExists() {
		return true;
	}

}
