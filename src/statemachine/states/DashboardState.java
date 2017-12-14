package statemachine.states;

import statemachine.core.StateMachine;
import statemachine.utils.StateNames;

public class DashboardState extends State {
	private StateMachine stateMachine;
	
	public DashboardState(StateMachine stateMachine) {
		this.stateMachine = stateMachine;
	}

	@Override
	public void execute() {
		// three different transitions
		
		// 1. Add file:
		//	
		//	Click button - Need reference to Add file button
		//	Transition to Add file state
		//	Change to add file scene
		
		// 2. Retrieve File Query:
		//	
		//	Click recommendation - Need reference to the ListView components
		//	Change to retrieve file state.
		//	Change to retrieve file scene.
		
		// 3. Viewing files:
		//
		// Click button - Need reference to button.
		// Change to retrieve file state.
		// Change to retrieve file scene.
	}
	
	private void addFile() {
		stateMachine.setCurrentState(StateNames.ADD_FILE.toString());
		// change scene to add file scene
	}
	
	private void retrieveFile() {
		stateMachine.setCurrentState(StateNames.RETRIEVE_FILE_QUERY.toString());
		// change scene to retrieve file query scene
	}
	
	private void viewFiles() {
		stateMachine.setCurrentState(StateNames.VIEWING_FILES.toString());
		// change scene to viewing files scene
	}
	
	private void refresh() {
		//get recommendations
		// Change observable list with new data.
	}

}
