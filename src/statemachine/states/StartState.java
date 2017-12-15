package statemachine.states;

import java.awt.Desktop;

import gui.core.GUI;
import gui.core.SceneContainerStage;
import gui.scenes.RetrieveRecommendationsScene;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.FileChooser;
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
	    	    		sceneContainerStage.changeScene(gui.getMyFilesScene());
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
//	    	    		FileChooser fileChooser = new FileChooser();
//	    	    		fileChooser.setTitle("Open Resource File");
//	    	    		fileChooser.showOpenDialog(sceneContainerStage);
	    	    		
	    	    		//TODO implement file chooser functionality
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
		return true;
	}

}
