package statemachine.states;

import statemachine.core.StateMachine;

public class RatingState extends State {
	private StateMachine stateMachine;
	
	public RatingState(StateMachine stateMachine) {
		this.stateMachine = stateMachine;
	}

	@Override
	public void execute() {
		// User clicks submit
		// or
		// user clicks back
	}
	
	private void clicksSubmit() {
		// rating score is recorded from rating object and written to file.
		// change to dashboard state
		// change to dashboard scene
	}
	
	private void clicksBack() {
		// change to dashboard state
		// change to dashboard scene
	}

}
