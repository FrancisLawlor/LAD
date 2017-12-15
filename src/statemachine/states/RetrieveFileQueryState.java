package statemachine.states;

import gui.core.GUI;
import gui.core.SceneContainerStage;
import statemachine.core.StateMachine;
import statemachine.utils.StateNames;

public class RetrieveFileQueryState extends State {
	StateMachine stateMachine;
	SceneContainerStage sceneContainerStage;
	GUI gui;
	
	public RetrieveFileQueryState(StateMachine stateMachine, SceneContainerStage sceneContainerStage, GUI gui) {
		this.stateMachine = stateMachine;
		this.sceneContainerStage = sceneContainerStage;
		this.gui = gui;
	}

	@Override
	public void execute() {
		sceneContainerStage.changeScene(gui.getFileRetrievalScene());
	}

	private void clicksYes() {
		stateMachine.setCurrentState(StateNames.RETRIEVING_FILE.toString());
		stateMachine.execute();
	}
	
	private void clicksNo() {
		stateMachine.setCurrentState(StateNames.DASHBOARD.toString());
		stateMachine.execute();
	}
}
