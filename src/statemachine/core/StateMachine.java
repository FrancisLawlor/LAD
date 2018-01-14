package statemachine.core;

import java.util.HashMap;
import java.util.Map;

import gui.core.GUI;
import gui.core.SceneContainerStage;
import peer.frame.core.PeerToPeerActorSystem;
import statemachine.states.AddFileState;
import statemachine.states.DashboardState;
import statemachine.states.LoadingSavedContentState;
import statemachine.states.RatingState;
import statemachine.states.RetrieveFileQueryState;
import statemachine.states.RetrieveRecommendationsState;
import statemachine.states.RetrievingFileState;
import statemachine.states.SetupState;
import statemachine.states.StartState;
import statemachine.states.State;
import statemachine.states.ViewingFilesState;
import statemachine.utils.StateName;

public class StateMachine {
	private String currentState;
	private Map<String, State> stateMap = new HashMap<String, State>();
	private SceneContainerStage containerStage = new SceneContainerStage();
	private GUI gui = new GUI(containerStage);
	
	public StateMachine(PeerToPeerActorSystem p2pActorSystem) {
		stateMap.put(StateName.START.toString(), new StartState(this, containerStage, gui, p2pActorSystem));
		stateMap.put(StateName.SETUP.toString(), new SetupState(this, containerStage, gui, p2pActorSystem));
		stateMap.put(StateName.RETRIEVE_RECOMMENDATIONS.toString(), new RetrieveRecommendationsState(this, containerStage, gui, p2pActorSystem));
		stateMap.put(StateName.DASHBOARD.toString(), new DashboardState(this, containerStage, gui));
		stateMap.put(StateName.ADD_FILE.toString(), new AddFileState(this, containerStage, gui, p2pActorSystem));
		stateMap.put(StateName.RETRIEVE_FILE_QUERY.toString(), new RetrieveFileQueryState(this, containerStage, gui));
		stateMap.put(StateName.RETRIEVING_FILE.toString(), new RetrievingFileState(this, containerStage, gui, p2pActorSystem));
		stateMap.put(StateName.RATING.toString(), new RatingState(this, containerStage, gui, p2pActorSystem));
		stateMap.put(StateName.VIEWING_FILES.toString(), new ViewingFilesState(this, containerStage, gui, p2pActorSystem));
		stateMap.put(StateName.LOADING_FILES.toString(), new LoadingSavedContentState(this, containerStage, gui, p2pActorSystem));
	}
	
	public void setCurrentState(String newState) {
		currentState = newState;
	}
	
	public void execute(StateName param) {
		stateMap.get(currentState).execute(param);
	}
}
