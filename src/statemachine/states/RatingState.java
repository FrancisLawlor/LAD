package statemachine.states;

import content.view.core.Rating;
import content.view.core.ViewingTime;
import gui.core.GUI;
import gui.core.SceneContainerStage;
import gui.utilities.GUIText;
import peer.frame.core.ViewerToUIChannel;
import statemachine.core.StateMachine;
import statemachine.utils.StateName;

public class RatingState extends State {
	private StateMachine stateMachine;
	private SceneContainerStage sceneContainerStage;
	private GUI gui;
	private ViewerToUIChannel viewer;
	private long viewingTimeStart;
	
	public RatingState(StateMachine stateMachine, SceneContainerStage sceneContainerStage, GUI gui, ViewerToUIChannel viewer) {
		this.stateMachine = stateMachine;
		this.sceneContainerStage = sceneContainerStage;
		this.gui = gui;
		this.viewer = viewer;
	}

	@Override
	public void execute(StateName param) {
		switch (param) {
		    case INIT:
		        this.init();
		        break;
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
	
	private void init() {
	    this.viewingTimeStart = System.currentTimeMillis();
	    this.gui.getRatingScene().getRating().setRating(0);
        sceneContainerStage.changeScene(gui.getRatingScene());
        sceneContainerStage.setTitle(GUIText.RATING);
	}
	
	private void clicksBack() {
        int timedView = (int)((System.currentTimeMillis() - this.viewingTimeStart) / 1000);
        ViewingTime viewingTime = new ViewingTime(timedView);
        this.viewer.recordContentViewInfo(viewingTime, null);
	    
		stateMachine.setCurrentState(StateName.DASHBOARD.toString());
		stateMachine.execute(StateName.INIT);
	}

	private void clicksSubmit() {
		Double score = gui.getRatingScene().getRating().getRating();
		Rating rating = new Rating(score);
		int timedView = (int)((System.currentTimeMillis() - this.viewingTimeStart) / 1000);
		ViewingTime viewingTime = new ViewingTime(timedView);
		this.viewer.recordContentViewInfo(viewingTime, rating);
		
		stateMachine.setCurrentState(StateName.DASHBOARD.toString());
		stateMachine.execute(StateName.INIT);
	}
}
