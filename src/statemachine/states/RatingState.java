package statemachine.states;

import gui.core.GUI;
import gui.core.SceneContainerStage;
import statemachine.core.StateMachine;

public class RatingState extends State {
	private StateMachine stateMachine;
	private SceneContainerStage sceneContainerStage;
	private GUI gui;
	
	public RatingState(StateMachine stateMachine, SceneContainerStage sceneContainerStage, GUI gui) {
		this.stateMachine = stateMachine;
		this.sceneContainerStage = sceneContainerStage;
		this.gui = gui;
	}

	@Override
	public void execute() {
		sceneContainerStage.changeScene(gui.getDashBoardScene());

		clicksSubmit();
	}
	
	private void clicksSubmit() {
		// rating score is recorded from rating object and written to file.
		// change to dashboard state
		// change to dashboard scene
	}
}
