package statemachine.states;

import gui.core.GUI;
import gui.core.SceneContainerStage;
import gui.utilities.GUIText;
import statemachine.core.StateMachine;
import statemachine.utils.StateName;

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
	public void execute(StateName param) {
		sceneContainerStage.changeScene(gui.getFileRetrievalScene());
		sceneContainerStage.setTitle(GUIText.FILE_RETRIEVAL);

		switch (param) {
			case CLICK_YES:
				clicksYes();
				break;
			case CLICK_NO:
				clicksNo();
				break;
			default:
				break;
		}
	}

	private void clicksYes() {
		stateMachine.setCurrentState(StateName.RETRIEVING_FILE.toString());
		stateMachine.execute(null);
	}
	
	private void clicksNo() {
		stateMachine.setCurrentState(StateName.DASHBOARD.toString());
		stateMachine.execute(null);
	}
}
