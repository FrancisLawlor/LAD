package statemachine.states;

import gui.core.GUI;
import gui.core.SceneContainerStage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import statemachine.core.StateMachine;
import statemachine.utils.StateName;

public class StartState extends State {
	private StateMachine stateMachine;
	private SceneContainerStage sceneContainerStage;
	private GUI gui;
	
	public StartState(StateMachine stateMachine, SceneContainerStage sceneContainerStage, GUI gui) {
		this.stateMachine = stateMachine;
		this.sceneContainerStage = sceneContainerStage;
		this.gui = gui;
		
		configureButtons();
	}

	private void configureButtons() {
		gui.getDashBoardScene().getMyFilesButton().setOnAction(new EventHandler<ActionEvent>() {
	    	    @Override public void handle(ActionEvent e) {
	    	    		stateMachine.execute(StateName.VIEWING_FILES);
	    	    }
	    	});
		
		gui.getDashBoardScene().getRefreshButton().setOnAction(new EventHandler<ActionEvent>() {
	    	    @Override public void handle(ActionEvent e) {
	    	    		stateMachine.execute(StateName.REFRESH);
	    	    }
	    	});
		
		gui.getDashBoardScene().getAddFileButton().setOnAction(new EventHandler<ActionEvent>() {
	    	    @Override public void handle(ActionEvent e) {
	    	    		stateMachine.execute(StateName.ADD_FILE);
	    	    }
	    	});
		
		gui.getFileRetrievalQueryScene().getNoButton().setOnAction(new EventHandler<ActionEvent>() {
	    	    @Override public void handle(ActionEvent e) {
	    	    		stateMachine.execute(StateName.CLICK_NO);
	    	    }
	    	});
		
		gui.getFileRetrievalQueryScene().getYesButton().setOnAction(new EventHandler<ActionEvent>() {
	    	    @Override public void handle(ActionEvent e) {
    	    			stateMachine.execute(StateName.CLICK_YES);
	    	    }
	    	});
		
		gui.getMyFilesScene().getBackButton().setOnAction(new EventHandler<ActionEvent>() {
	    	    @Override public void handle(ActionEvent e) {
	    	    		stateMachine.execute(StateName.CLICK_BACK);
	    	    }
		});
		
		gui.getRatingScene().getBackButton().setOnAction(new EventHandler<ActionEvent>() {
	    	    @Override public void handle(ActionEvent e) {
	    	    		stateMachine.execute(StateName.CLICK_BACK);
	    	    }
		});
		
		gui.getRatingScene().getSubmitButton().setOnAction(new EventHandler<ActionEvent>() {
	    	    @Override public void handle(ActionEvent e) {
	    	    		stateMachine.execute(null);
	    	    }
		});
		
		gui.getSetupScene().getNextButton().setOnAction(new EventHandler<ActionEvent>() {
	    	    @Override public void handle(ActionEvent e) {
	    	    		stateMachine.execute(null);
	    	    }
		});
	}

	@Override
	public void execute(StateName param) {
		if (!configFileExists()) {
			stateMachine.setCurrentState(StateName.SETUP.toString());
			sceneContainerStage.init(gui.getSetupScene());
			sceneContainerStage.show();
		} else {
			stateMachine.setCurrentState(StateName.RETRIEVE_RECOMMENDATIONS.toString());
			sceneContainerStage.init(gui.getRetrieveRecommendationsScene());
			sceneContainerStage.show();
		}
	}
	
	// TODO
	private boolean configFileExists() {
		return false;
	}

}
