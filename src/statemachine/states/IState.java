package statemachine.states;

import statemachine.utils.StateName;

public interface IState {
	void execute(StateName param);
}
