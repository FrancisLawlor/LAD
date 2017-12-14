package statemachine.states;

import statemachine.core.StateMachine;
import statemachine.utils.StateNames;

public class RetrievingFileState extends State {
	StateMachine stateMachine;
	
	public RetrievingFileState(StateMachine stateMachine) {
		super(stateMachine);
	}

	@Override
	public void execute() {
		// start downloading file from other user
		// listen for download to stop finishing
		// when download finishes change state to Rating state
		// change scene to rating scene
	}
	
	public void downloadFinished() {
		stateMachine.setCurrentState(StateNames.RATING.toString());
		// change to rating scene
	}

}
