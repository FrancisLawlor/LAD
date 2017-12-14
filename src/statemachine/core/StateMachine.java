package statemachine.core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import content.content.Content;
import statemachine.states.AddFileState;
import statemachine.states.DashboardState;
import statemachine.states.RatingState;
import statemachine.states.RetrieveFileQueryState;
import statemachine.states.RetrieveRecommendationsState;
import statemachine.states.RetrievingFileState;
import statemachine.states.SetupState;
import statemachine.states.StartState;
import statemachine.states.State;
import statemachine.states.ViewingFilesState;
import statemachine.utils.StateNames;

public class StateMachine {
	private String currentState;
	private Map<String, State> stateMap = new HashMap<String, State>();
	
	StateMachine() {
		stateMap.put(StateNames.START.toString(), new StartState());
		stateMap.put(StateNames.SETUP.toString(), new SetupState());
		stateMap.put(StateNames.RETRIEVE_RECOMMENDATIONS.toString(), new RetrieveRecommendationsState());
		stateMap.put(StateNames.DASHBOARD.toString(), new DashboardState());
		stateMap.put(StateNames.ADD_FILE.toString(), new AddFileState());
		stateMap.put(StateNames.RETRIEVE_FILE_QUERY.toString(), new RetrieveFileQueryState());
		stateMap.put(StateNames.RETRIEVING_FILE.toString(), new RetrievingFileState());
		stateMap.put(StateNames.RATING.toString(), new RatingState());
		stateMap.put(StateNames.VIEWING_FILES.toString(), new ViewingFilesState());
	}
	
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
