package statemachine.states;

import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import content.content.Content;
import statemachine.core.StateMachine;
import statemachine.utils.StateNames;

public class RetrieveRecommendationsState extends State {
	private StateMachine stateMachine;
	private int TIME_OUT = 10000;

	public RetrieveRecommendationsState(StateMachine stateMachine) {
		super(stateMachine);
	}

	@Override
	public void execute() {
		Iterator<Content> recommendations;
		try {
			recommendations = retrieveRecommendations();
		} catch (InterruptedException | TimeoutException e) {
			e.printStackTrace();
		}
		
		// Put recommendations in to observable list for Dashboard.
		stateMachine.setCurrentState(StateNames.DASHBOARD.toString());
		// Change to Dashboard scene
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
                // TODO tell user it has timed out and close program.
            }
		}
		
		return recommendations;
	}
	
	private Iterator<Content> getRecommendations() {
		// TODO
		return null;
	}

}
