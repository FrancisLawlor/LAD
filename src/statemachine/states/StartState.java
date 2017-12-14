package statemachine.states;

import gui.core.SceneContainerStage;
import gui.scenes.RetrieveRecommendationsScene;
import gui.scenes.SetupScene;
import statemachine.core.StateMachine;
import statemachine.utils.StateNames;

public class StartState extends State {
	private StateMachine stateMachine;
	private SceneContainerStage sceneContainerStage;
	
	public StartState(StateMachine stateMachine, SceneContainerStage sceneContainerStage) {
		this.stateMachine = stateMachine;
		this.sceneContainerStage = sceneContainerStage;
	}

	@Override
	public void execute() {
		if (!configFileExists()) {
			stateMachine.setCurrentState(StateNames.SETUP.toString());
			sceneContainerStage.init(new SetupScene(sceneContainerStage));
			sceneContainerStage.show();
		} else {
			stateMachine.setCurrentState(StateNames.RETRIEVE_RECOMMENDATIONS.toString());
			sceneContainerStage.init(new RetrieveRecommendationsScene());
			sceneContainerStage.show();
		}
	}
	
	// TODO
	private boolean configFileExists() {
		return false;
	}

}
