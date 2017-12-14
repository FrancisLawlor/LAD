package statemachine.states;

import statemachine.core.StateMachine;
import statemachine.utils.StateNames;

public class RetrieveFileQueryState extends State {
	StateMachine stateMachine;
	
	public RetrieveFileQueryState(StateMachine stateMachine) {
		super(stateMachine);
	}

	@Override
	public void execute() {
		// User clicks yes
		// change to retrieving file state
		
		// User clicks no
		// change back to dashboard
	}

	private void clicksYes() {
		stateMachine.setCurrentState(StateNames.RETRIEVING_FILE.toString());
		// change to retrieving file scene
	}
	
	private void clicksNo() {
		stateMachine.setCurrentState(StateNames.DASHBOARD.toString());
		// change to retrieving file scene
	}
}
