package statemachine.states;

import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import content.content.Content;
import statemachine.core.StateMachine;
import statemachine.utils.StateNames;

public class SetupState extends State {
	private StateMachine stateMachine;
	private int TIME_OUT = 10000;
	
	SetupState(StateMachine stateMachine) {
		super(stateMachine);
	}

	@Override
	public void execute() {
		Iterator<Content> recommendations;
		try {
			recommendations = retrieveRecommendations();
		} catch (InterruptedException | TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Make recommendations available to Dashboard scene
		
		stateMachine.setCurrentState(StateNames.DASHBOARD.toString());
	}
	
	private Iterator<Content> retrieveRecommendations() throws InterruptedException, TimeoutException {
		int i = 0;
		
		Iterator<Content> recommendations = null;
		
		while (i != TIME_OUT) {
			recommendations = getRecommendations();
			if (recommendations != null) {
				break;
			} else {
	            TimeUnit.SECONDS.sleep(1);
	            i++;
			}
			if (i == TIME_OUT) {
                throw new TimeoutException("Timed out..");
            }
		}
		
		return recommendations;
	}
	
	Iterator<Content> getRecommendations() {
		// TODO
		return null;
	}
}
