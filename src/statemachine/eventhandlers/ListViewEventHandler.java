package statemachine.eventhandlers;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import statemachine.core.StateMachine;
import statemachine.utils.StateName;

public class ListViewEventHandler implements EventHandler<MouseEvent> {
	private StateName stateName;
	private StateMachine stateMachine;
	
	public ListViewEventHandler(StateName stateName, StateMachine stateMachine) {
		this.stateName = stateName;
		this.stateMachine = stateMachine;
	}
	
	@Override
	public void handle(MouseEvent event) {
		stateMachine.execute(stateName);
	}
	
}
