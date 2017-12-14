package statemachine.states;

import statemachine.core.StateMachine;

public class State implements IState {
	private StateMachine stateMachine;
	
	State(StateMachine stateMachine) {
		this.stateMachine = stateMachine;
	}
	public void execute() {
		
	}
}
