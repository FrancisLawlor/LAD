package statemachine.states;

import gui.core.GUI;
import gui.core.SceneContainerStage;
import gui.scenes.RetrieveRecommendationsScene;
import statemachine.core.StateMachine;
import statemachine.utils.StateNames;

public class StartState extends State {
	private StateMachine stateMachine;
	private SceneContainerStage sceneContainerStage;
	private GUI gui;
	
	public StartState(StateMachine stateMachine, SceneContainerStage sceneContainerStage, GUI gui) {
		this.stateMachine = stateMachine;
		this.sceneContainerStage = sceneContainerStage;
		this.gui = gui;
	}

	@Override
	public void execute() {
		if (!configFileExists()) {
			stateMachine.setCurrentState(StateNames.SETUP.toString());
			sceneContainerStage.init(gui.getSetupScene());
			sceneContainerStage.show();
		} else {
			stateMachine.setCurrentState(StateNames.RETRIEVE_RECOMMENDATIONS.toString());
			sceneContainerStage.init(gui.getRetrieveRecommendationsScene());
			sceneContainerStage.show();
		}
	}
	
	// TODO
	private boolean configFileExists() {
		return true;
	}

}
