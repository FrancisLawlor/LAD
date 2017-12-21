package statemachine.states;

import gui.core.GUI;
import gui.core.SceneContainerStage;
import gui.utilities.GUIText;
import statemachine.core.StateMachine;
import statemachine.utils.StateName;

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
	public void execute(StateName param) {
		sceneContainerStage.changeScene(gui.getRatingScene());
		sceneContainerStage.setTitle(GUIText.RATING);

		switch (param) {
			case CLICK_SUBMIT:
				clicksSubmit();
				break;
			case CLICK_BACK:
				clicksBack();
				break;
			default:
				break;
		}
	}
	
	private void clicksBack() {
		stateMachine.setCurrentState(StateName.DASHBOARD.toString());
		stateMachine.execute(StateName.INIT);
	}

	private void clicksSubmit() {
		Double score = gui.getRatingScene().getRating().getRating();
		
		// TODO Write this score to file
		
		stateMachine.setCurrentState(StateName.DASHBOARD.toString());
		stateMachine.execute(StateName.INIT);
	}
}
