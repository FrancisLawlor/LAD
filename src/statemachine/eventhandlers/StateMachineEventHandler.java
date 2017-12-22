package statemachine.eventhandlers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import statemachine.core.StateMachine;
import statemachine.utils.StateName;

public class StateMachineEventHandler implements EventHandler<ActionEvent> {
	private StateName stateName;
	private StateMachine stateMachine;
	
	public StateMachineEventHandler(StateName stateName, StateMachine stateMachine) {
		this.stateName = stateName;
		this.stateMachine = stateMachine;
	}
	@Override
	public void handle(ActionEvent event) {
		stateMachine.execute(stateName);
	}
	
}
