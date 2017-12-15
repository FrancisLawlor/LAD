package statemachine.states;

import gui.core.GUI;
import gui.core.SceneContainerStage;
import statemachine.core.StateMachine;

public class ViewingFilesState extends State {
	StateMachine stateMachine;
	SceneContainerStage sceneContainerStage;
	GUI gui;
	
	public ViewingFilesState(StateMachine stateMachine, SceneContainerStage sceneContainerStage, GUI gui) {
		this.stateMachine = stateMachine;
		this.sceneContainerStage = sceneContainerStage;
		this.gui = gui;
	}

	@Override
	public void execute() {
		sceneContainerStage.changeScene(gui.getMyFilesScene());
	}
	
	private void clicksBack() {
		// changes state to Dashboard
		// changes scene to Dashboard
	}
	
	private void clicksFile() {
		// file opens
		// change to rating state
		// change to rating scene
	}

}
