package statemachine.core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import content.content.Content;

public class StateMachine {
	private String currentState;
	private Map<String, State> stateMap = new HashMap<String, State>();
	
	public void setCurrentState(String newState) {
		currentState = newState;
	}
	
	public void execute() {
		stateMap.get(currentState).execute();
	}
	
    public void setRecommendationsForUser(Iterator<Content> recommendations) {
        
    }
    
    public void setRetrievedContent(Content content) {
        
    }
}
