package statemachine.states;

import gui.core.GUI;
import gui.core.SceneContainerStage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
		
		configureButtons();
	}

	private void configureButtons() {
		gui.getDashBoardScene().getMyFilesButton().setOnAction(new EventHandler<ActionEvent>() {
	    	    @Override public void handle(ActionEvent e) {
	    	    		stateMachine.setCurrentState(StateNames.VIEWING_FILES.toString());
	    	    		stateMachine.execute();
	    	    }
	    	});
		
		gui.getDashBoardScene().getRefreshButton().setOnAction(new EventHandler<ActionEvent>() {
	    	    @Override public void handle(ActionEvent e) {
	    	    		//TODO add logic for refresh
	    	    }
	    	});
		
		gui.getDashBoardScene().getAddFileButton().setOnAction(new EventHandler<ActionEvent>() {
	    	    @Override public void handle(ActionEvent e) {
	    	    		stateMachine.setCurrentState(StateNames.ADD_FILE.toString());
	    	    		stateMachine.execute();
	    	    }
	    	});
		
		gui.getFileRetrievalQueryScene().getNoButton().setOnAction(new EventHandler<ActionEvent>() {
	    	    @Override public void handle(ActionEvent e) {
	    	    		stateMachine.setCurrentState(StateNames.DASHBOARD.toString());
	    	    		stateMachine.execute();
	    	    }
	    	});
		
		gui.getFileRetrievalQueryScene().getYesButton().setOnAction(new EventHandler<ActionEvent>() {
	    	    @Override public void handle(ActionEvent e) {
	    	    		stateMachine.setCurrentState(StateNames.RETRIEVING_FILE.toString());
	    	    		stateMachine.execute();
	    	    }
	    	});
		
		gui.getMyFilesScene().getBackButton().setOnAction(new EventHandler<ActionEvent>() {
	    	    @Override public void handle(ActionEvent e) {
	    	    		stateMachine.setCurrentState(StateNames.DASHBOARD.toString());
	    	    		stateMachine.execute();
	    	    }
		});
		
		gui.getRatingScene().getBackButton().setOnAction(new EventHandler<ActionEvent>() {
	    	    @Override public void handle(ActionEvent e) {
	    	    		stateMachine.setCurrentState(StateNames.DASHBOARD.toString());
	    	    		stateMachine.execute();
	    	    }
		});
		
		gui.getRatingScene().getSubmitButton().setOnAction(new EventHandler<ActionEvent>() {
	    	    @Override public void handle(ActionEvent e) {
	    	    		stateMachine.execute();
	    	    }
		});
		
		gui.getSetupScene().getNextButton().setOnAction(new EventHandler<ActionEvent>() {
	    	    @Override public void handle(ActionEvent e) {
    	    			stateMachine.setCurrentState(StateNames.RETRIEVING_FILE.toString());
    	    			sceneContainerStage.init(gui.getRetrieveRecommendationsScene());
	    	    		stateMachine.execute();
	    	    }
		});
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
		return false;
	}

}
