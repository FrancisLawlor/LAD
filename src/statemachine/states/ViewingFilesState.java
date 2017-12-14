package statemachine.states;

import statemachine.core.StateMachine;

public class ViewingFilesState extends State {
	StateMachine stateMachine;
	
	public ViewingFilesState(StateMachine stateMachine) {
		super(stateMachine);
	}

	@Override
	public void execute() {
		
	}
	
	private void clicksBack() {
		// changes state to Dashboard
		// changes scene to Dashboard
	}
	
	private void clicksFile() {
		// file opens
		// change to rating state
		// change to rating scene
	}

}
