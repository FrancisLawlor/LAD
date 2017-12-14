package statemachine.states;

import java.io.File;

import statemachine.core.StateMachine;
import statemachine.utils.StateNames;

public class AddFileState extends State {
	StateMachine stateMachine;
	
	public AddFileState(StateMachine stateMachine) {
		this.stateMachine = stateMachine;
	}

	@Override
	public void execute() {
		//Open filechooser
		//file is wrapped
		//Return to dashboard
		
		stateMachine.setCurrentState(StateNames.DASHBOARD.toString());
		// change to dashboard scene
	}
	
	private File chooseFile() {
		// filechooser opens
		// file is selected
		// possibly restricted to certain types - check type.
		// return file
		
		return null;
	}
	
	private void wrapFile() {
		// file is wrapped and returned;
	}

}
